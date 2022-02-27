package com.ame.rest.extension;

import java.util.Map;
import java.util.Set;

import com.ame.rest.extension.Extension.LINK_TYPE;
import com.ame.rest.extension.instance.Instance;
import com.ame.rest.user.developer.Developer;
import com.ame.rest.util.dto.DTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BrowseExtensionDTO implements DTO{

    Long id;
    String name;
    String description;

    @JsonIgnore
    Developer developer;

    @JsonIgnore
    Map<Extension.LINK_TYPE, String> links;

    @JsonIgnore
    Set<Instance> instances;

    public String getDeveloperEmail() {
        return this.developer.getEmail();
    }

    public String getExtensionWebsite() {
        if (links.containsKey(LINK_TYPE.WEBSITE)) {
            return links.get(LINK_TYPE.WEBSITE);
        }

        return "";
    }

    public int getExecutionCount() {

        int total = 0;
        for (Instance instance : instances) {
            total = total + instance.getExecuteCounter();
        }

        return total;
    }

    public String getDeveloperName() {
        return developer.getEmail();
    }

    public int getInstanceCount() {
        return instances.size();
    }

    @Override
    public DTO_TYPE getDtoType() {
        return DTO_TYPE.BROWSE_EXTENSION;
    }

    @Override
    public String getOriginalName() {
        return "Extension";
    }
}
