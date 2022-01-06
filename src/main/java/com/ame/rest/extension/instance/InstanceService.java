package com.ame.rest.extension.instance;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.ame.rest.exceptions.UnauthorizedAccessAttempt;
import com.ame.rest.extension.Extension;
import com.ame.rest.extension.ExtensionService;
import com.ame.rest.extension.instance.Instance.STATE;
import com.ame.rest.user.UserService;
import com.ame.rest.util.DTOFactory;
import com.ame.rest.util.dto.DTO;
import com.ame.rest.util.dto.InstanceDTO;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class InstanceService {

    // move to service
    @Autowired
    InstanceRepository repo;

    @Autowired
    UserService userService;

    @Autowired
    ExtensionService extensionService;

    @Autowired
    DTOFactory dtoFactory;
    
    @Bean
    public WebClient.Builder clientBuilder() {
        return WebClient.builder();
    }

    public void CreateInstance(Long extensionId) throws Exception {
        Extension extension = extensionService.findById(extensionId);
        Instance instance = new Instance(extension, userService.getCurrentWriter());
        repo.save(instance);
    }

    public List<InstanceDTO> getInstances() throws Exception {
        List<Instance> instances = userService.getCurrentWriter().getInstances();

        instances = instances.stream().filter(e -> e.getState() != Instance.STATE.DELETED) // don't add deleted instances to the list
        .collect(Collectors.toList());

        return (List<InstanceDTO>)dtoFactory.getDto(instances,DTO.DTO_TYPE.INSTANCE);
    }

    // public InstanceDTO convertToDTO(Instance instance) {
    //     InstanceDTO instanceDTO = modelMapper.map(instance, InstanceDTO.class);
    //     return instanceDTO;
    // }

    public String runInstance(Long id) throws Exception {
        // get the instance and run
        Instance instance = repo.findById(id);

        // check if the instance is paused or deleted
        //TODO ask if the writer wnats to share thier email but for now just post it
        if (instance.getState() != Instance.STATE.OPEN) {
            return "instance is not live, contact it's create @ "+instance.getWriter().getEmail();
        }

        String data = clientBuilder().build().get().uri(instance.getExtension().getLinks().get(Extension.LINK_TYPE.EXECUTE))
                .retrieve().bodyToMono(ExecuteResponse.class).block().getDocument();

        // update counter
        instance.setExecuteCounter(instance.getExecuteCounter()+1);
        repo.save(instance);

        return data;

    }

    //TODO, improve extension to instance authentication
    public String getData(Map<String, String> request) throws Exception{
        Long id = Long.parseLong(request.get("id"));
        String key = request.get("key");

        Instance instance = repo.findById(id);
        if (instance.getInstanceKey().equals(key)) {
            return instance.getData();
        }

        throw new UnauthorizedAccessAttempt("you are not permitted to access this instance data");
    }

    public void setData(Map<String, String> request) throws Exception{
        Long id = Long.parseLong(request.get("id"));
        String key = request.get("key");
        String data =  request.get("data");

        Instance instance = repo.findById(id);
        if (instance.getInstanceKey().equals(key)) {
            instance.setData(data);
            repo.save(instance);
        }

        throw new UnauthorizedAccessAttempt("you are not permitted to access this instance data");
    }

    public void updateState(Long id, String state) throws Exception{

        // TODO
        // DELETE should delete data and signal extension api to do the same if applicable
        // PAUSE will just prevent requests until extension is open again

        Instance instance = repo.findById(id);
        STATE eState = STATE.valueOf(state);
        instance.setState(eState);
        repo.save(instance);
    }

    public String getExecutionLink(Long id){
       return "<iframe frameBorder='0'  width='100%' height='100%' src='http://localhost:8080/instance/run/" + id + "'/>";
    }
}
