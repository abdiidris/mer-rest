package com.ame.rest.extension;

import java.util.Map;
import java.util.Set;

import com.ame.rest.extension.Extension.LINK_TYPE;
import com.ame.rest.extension.instance.Copy;
import com.ame.rest.extension.instance.Instance;
import com.ame.rest.user.developer.Developer;
import com.ame.rest.extension.instance.Copy;
import com.ame.rest.util.dto.DTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BrowseExtensionDTO extends DTO{

    String name;
    String description;

    @JsonIgnore
    Developer developer;

    @JsonIgnore
    Map<Extension.LINK_TYPE, String> links;

    @JsonIgnore
    Set<Instance> instances;

    @Override
    public DTO_TYPE getDtoType() {
        return DTO_TYPE.BROWSE_EXTENSION;
    }

    @Override
    public String getOriginalName() {
        return "Extension";
    }
}
