package com.ame.rest.util.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.ame.rest.extension.Extension;
import com.ame.rest.extension.Extension.LINK_TYPE;
import com.ame.rest.extension.instance.Instance;
import com.ame.rest.extension.instance.InstanceService;
import com.ame.rest.user.developer.Developer;
import com.ame.rest.util.dto.DTO.DTO_TYPE;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class DevelopExtensionDTO implements DTO{

    Long id;
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
        for (Instance instance : instances) {
            activeLinks.add(instanceService.getExecutionLink(instance.getId()));
        }
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
