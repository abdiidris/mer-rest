package com.ame.rest.extension.instance;

import java.util.HashMap;
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

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
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

    instances = instances.stream().filter(e -> e.getState() != Instance.STATE.DELETED) // don't add deleted instances to
                                                                                       // the list
        .collect(Collectors.toList());

    return (List<InstanceDTO>) dtoFactory.getDto(instances, DTO.DTO_TYPE.INSTANCE);
  }

  // public InstanceDTO convertToDTO(Instance instance) {
  // InstanceDTO instanceDTO = modelMapper.map(instance, InstanceDTO.class);
  // return instanceDTO;
  // }

  public String runInstance(Long id) throws Exception {
    // get the instance and run
    Instance instance = repo.findById(id);

    // check if the instance is paused or deleted
    // TODO ask if the writer wnats to share thier email but for now just post it
    if (instance.getState() != Instance.STATE.OPEN) {
      return "instance is not live, contact it's create @ " + instance.getWriter().getEmail();
    }

    HashMap<String, String> body = new HashMap<>();
    body.put("data", instance.getData());

    String data = clientBuilder().build().post().uri(instance.getExtension().getLinks().get(Extension.LINK_TYPE.EXECUTE)).body(BodyInserters.fromValue(body))
        .retrieve().bodyToMono(ExecuteResponse.class).block().getDocument();

    // update counter
    instance.setExecuteCounter(instance.getExecuteCounter() + 1);
    repo.save(instance);

    return data;

  }

  public String getData(String id_key) throws Exception {
    String[] separated_id_key = id_key.split("_");
    Long id = Long.parseLong(separated_id_key[0]);

    String key = separated_id_key[1];
    Instance instance = repo.findById(id);
    if (instance.getInstanceKey().toString().equals(key)) {
      return instance.getData();
    }

    throw new UnauthorizedAccessAttempt("you are not permitted to access this instance data");
  }

  public String setData(Map<String, String> request) throws Exception {
    String[] separated_id_key = request.get("key").split("_");
    Long id = Long.parseLong(separated_id_key[0]);
    String key = separated_id_key[1];
    String data = request.get("data");

    Instance instance = repo.findById(id);
    if (instance.getInstanceKey().toString().equals(key)) {
      instance.setData(data);
      repo.save(instance);
      return "success";
    }

    throw new UnauthorizedAccessAttempt("you are not permitted to access this instance data");
  }

  public void updateState(Long id, String state) throws Exception {

    // TODO DELETE should delete data and signal extension api to do the same if
    // applicable
    // PAUSE will just prevent requests until extension is open again

    Instance instance = repo.findById(id);
    STATE eState = STATE.valueOf(state);
    instance.setState(eState);
    repo.save(instance);
  }

}
