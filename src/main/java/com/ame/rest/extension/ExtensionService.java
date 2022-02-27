package com.ame.rest.extension;
import java.util.List;

import com.ame.rest.exceptions.MissingParameterException;
import com.ame.rest.user.UserService;
import com.ame.rest.util.DTOFactory;
import com.ame.rest.util.dto.DTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ExtensionService {

    @Autowired
    private ExtensionRepository repo;

    @Autowired
    private UserService userService;

    @Autowired
    DTOFactory dtoFactory;

    public void registerExtension(Extension extension) throws Exception {

        extension.setDeveloper(userService.getCurrentDeveloper());
        // ensure the website and execute links are present
        String websiteLink = extension.getLinks().get(Extension.LINK_TYPE.WEBSITE);
        String executeLink = extension.getLinks().get(Extension.LINK_TYPE.EXECUTE);

        if (!StringUtils.hasText(websiteLink) || !StringUtils.hasText(executeLink)) {
            throw new MissingParameterException(
                    "Website link and the execution link are required to create a new extension");
        }

        repo.save(extension);
    }

    public List<DevelopExtensionDTO> findByDeveloper() throws Exception {
        return  (List<DevelopExtensionDTO>) dtoFactory.getDto(repo.findByDeveloper(userService.getCurrentDeveloper()), DTO.DTO_TYPE.DEVELOP_EXTENSION); 
    }

    public List<BrowseExtensionDTO> findAll() {
        return   (List<BrowseExtensionDTO>) dtoFactory.getDto(repo.findAll(), DTO.DTO_TYPE.BROWSE_EXTENSION); 
    }

    public Extension findById(Long id) {
        return repo.findById(id);
    }

}
