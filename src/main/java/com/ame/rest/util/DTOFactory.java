package com.ame.rest.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ame.rest.extension.BrowseExtensionDTO;
import com.ame.rest.extension.DevelopExtensionDTO;
import com.ame.rest.extension.instance.InstanceDTO;
import com.ame.rest.util.dto.DTO;
import com.ame.rest.util.dto.DTO.DTO_TYPE;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Takes in an object or List and returns the DTO requested if it is available
@Component
public class DTOFactory {

    @Autowired
    private ModelMapper modelMapper;

    public DTO getDto(Object entity, DTO_TYPE type) {
        switch (type) {
            case BROWSE_EXTENSION:
                return modelMapper.map(entity, BrowseExtensionDTO.class);
            case DEVELOP_EXTENSION:
                return modelMapper.map(entity, DevelopExtensionDTO.class);
            case INSTANCE:
                return modelMapper.map(entity, InstanceDTO.class);
            default:
                break;
        }
        return null;
    }

    public List<? extends DTO> getDto(List<?> entities, DTO_TYPE type, Map<Long,Map<String,Object>> additional) {

        List<DTO> dtoList = new ArrayList<>();

        for (Object entity : entities) {
            DTO dto = getDto(entity, type);

            if (additional.containsKey(dto.getId())) {
                dto.setAdditional(additional.get(dto.getId()));
            }

            dtoList.add(dto);
        }

        return dtoList;
    }
}
