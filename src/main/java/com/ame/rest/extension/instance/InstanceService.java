package com.ame.rest.extension.instance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ame.rest.exceptions.UnauthorizedAccessAttempt;
import com.ame.rest.extension.Extension;
import com.ame.rest.extension.ExtensionService;
import com.ame.rest.extension.Extension.LINK_TYPE;
import com.ame.rest.extension.instance.Instance.STATE;
import com.ame.rest.user.UserService;
import com.ame.rest.util.DTOFactory;
import com.ame.rest.util.dto.DTO;

import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Date;

@Service
public class InstanceService {

  // move to service
  @Autowired
  InstanceRepository repo;

  @Autowired
  CopyRepo copyRepo;

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

  public Integer getExecutionCount(Instance instance) {
    Integer executionCount = 0;
    for (Copy c : instance.getCopies()) {
      executionCount += c.getExecutions().size();
    }

    return executionCount;
  }

  public Map<String, Object> setAdditionalDtoInfo(Instance instance) {
    Map<String, Object> additional = new HashMap<String, Object>();

    // remove the execution link for an extension. This is accessed through the copy
    // execution method and is not needed by the client when browsing instances
    Map<LINK_TYPE, String> links = instance.getExtension().getLinks();
    links.remove(LINK_TYPE.EXECUTE);

    additional.put("links", links);
    additional.put("executionCount", getExecutionCount(instance));
    return additional;

  }

  public List<InstanceDTO> getInstances() throws Exception {
    List<Instance> instances = userService.getCurrentWriter().getInstances();

    instances.removeIf(i -> i.getState() == Instance.STATE.DELETE);
    // additional information about each instance for the client
    Map<Long, Map<String, Object>> additional = new HashMap<Long, Map<String, Object>>();

    for (Instance i : instances) {
      additional.put(i.getId(), setAdditionalDtoInfo(i));
    }

    return (List<InstanceDTO>) dtoFactory.getDto(instances, DTO.DTO_TYPE.INSTANCE, additional);
  }

  public String runInstance(Long id) throws Exception {
    // get the instance and run
    Copy copy = copyRepo.findById(id);

    Instance instance = copy.getInstance();

    // check if the instance is paused or deleted
    // TODO ask if the writer wants to share thier email but for now just post it
    if (instance.getState() != Instance.STATE.OPEN) {
      return "instance is not live, contact it's creator @ " + instance.getWriter().getEmail();
    }

    HashMap<String, String> body = new HashMap<>();
    body.put("data", instance.getData());

    String data = clientBuilder().build().post()
        .uri(instance.getExtension().getLinks().get(Extension.LINK_TYPE.EXECUTE)).body(BodyInserters.fromValue(body))
        .retrieve().bodyToMono(ExecuteResponse.class).block().getDocument();

    // update counter
    copy.getExecutions().add(new Date());

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

    // convert the names to that found in the enum
    Instance instance = repo.findById(id);
    STATE eState = STATE.valueOf(state);
    instance.setState(eState);
    repo.save(instance);
  }

  public void createCopy(Map<String, String> request) {
    Instance i = repo.findById(Long.parseLong(request.get("instanceId")));

    if (!validateUser(i)) return;

    Copy c = new Copy(request.get("description"), i);
    List<Copy> copyList = i.getCopies();

    copyList.add(c);
    copyRepo.save(c);
    repo.save(i);
  }

  public boolean validateUser(Instance instance){
    return userService.compareUser(instance.getWriter());
  }
  public void updateDetails(Map<String, String> changes) {

    Long id = Long.parseLong(changes.get("id"));
    Instance instance = repo.findById(id);

    if (!validateUser(instance)) return;

    instance.setInstanceName(changes.get("instanceName"));

    repo.save(instance);
  }

}
