package com.ame.rest.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.ame.rest.extension.instance.Instance;
import com.ame.rest.extension.instance.InstanceService;
import com.ame.rest.user.developer.Developer;
import com.ame.rest.util.dto.DTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class DevelopExtensionDTO extends DTO{

    // Long id;
    String name;
    String description;
    Map<Extension.LINK_TYPE, String> links;

    @JsonIgnore
    Developer developer;
    @JsonIgnore
    List<Instance> instances;
    @Autowired @JsonIgnore
    InstanceService instanceService;

    public List<String> getActiveLinks(){
        List<String> activeLinks = new ArrayList<String>();
        return activeLinks;
    }

    @Override
    public DTO_TYPE getDtoType() {
        return DTO_TYPE.DEVELOP_EXTENSION;
    }

    @Override
    public String getOriginalName() {
        return "Extension";
    }
}
