package com.ame.rest.extension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ame.rest.extension.instance.Instance;
import com.ame.rest.extension.instance.InstanceService;
import com.ame.rest.user.UserService;
import com.ame.rest.util.DTOFactory;
import com.ame.rest.util.dto.DTO;
import com.ame.rest.util.exceptions.MissingParameterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ExtensionService {

    public static final String REGISTRATION_SUCCESS = "Extension registered";
    public static final String MISSING_LINKS = "Website link and the execution link are required to create a new extension";
    public static final String NAME_IS_TAKEN = "Extension name is taken";

    @Autowired
    private ExtensionRepository repo;

    @Autowired
    private UserService userService;

    @Autowired
    private InstanceService instanceService;

    @Autowired
    DTOFactory dtoFactory;

    public String registerExtension(Extension extension) throws Exception {

        extension.setDeveloper(userService.getCurrentDeveloper());
        // ensure the website and execute links are present
        // String websiteLink = extension.getLinks().get(Extension.LINK_TYPE.WEBSITE);
        String executeLink = extension.getLinks().get(Extension.LINK_TYPE.EXECUTE);

        if (!StringUtils.hasText(executeLink)) {
            throw new MissingParameterException(
            MISSING_LINKS);
        }

  
            repo.save(extension);
      
        

        return REGISTRATION_SUCCESS;
    }

    public int getExecutionCount(Extension ext) {
        int total = 0;
        for(Instance i: ext.getInstances()){
            total += instanceService.getExecutionCount(i);
        }
        return total;
    }

    // this info will be attached to the browseExtensionDto object when the client requests to browse all extensions
    public Map<String, Object> setAdditionalDtoInfo(Extension extension){
        Map<String, Object> info = new HashMap<String, Object>();
        info.put("developerEmail",extension.getDeveloper().getEmail());
        info.put("executionCount",getExecutionCount(extension));
        info.put("instanceCount",extension.getInstances().size());
        return info;

    }

    public List<BrowseExtensionDTO> findAll() {
        List<Extension> extensions = repo.findAll();
        Map<Long, Map<String, Object>> additional = new HashMap<Long, Map<String, Object>>();
        for(Extension ext: extensions){
            additional.put(ext.getId(), setAdditionalDtoInfo(ext));
        }
        List<BrowseExtensionDTO> dtos = (List<BrowseExtensionDTO>) dtoFactory.getDto(extensions, DTO.DTO_TYPE.BROWSE_EXTENSION,additional); 

        return dtos;
    }
    public List<DevelopExtensionDTO> findByDeveloper() throws Exception {
        return  (List<DevelopExtensionDTO>) dtoFactory.getDto(repo.findByDeveloper(userService.getCurrentDeveloper()), DTO.DTO_TYPE.DEVELOP_EXTENSION); 
    }

    public Extension findById(Long id) {
        return repo.findById(id);
    }

}
