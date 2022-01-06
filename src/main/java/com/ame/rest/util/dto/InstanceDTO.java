package com.ame.rest.util.dto;

import java.sql.Date;

import com.ame.rest.extension.Extension;
import com.ame.rest.extension.Extension.LINK_TYPE;
import com.ame.rest.extension.instance.Instance;
import com.ame.rest.extension.instance.InstanceService;
import com.ame.rest.extension.instance.Instance.STATE;
import com.ame.rest.user.writer.Writer;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstanceDTO implements DTO{

    Long id;
    @JsonIgnore
    Extension extension;
    @JsonIgnore
    Writer writer;

    @Autowired @JsonIgnore
    InstanceService service;

    int executeCounter;
    Date createDate;

    Instance.STATE state;

    public String getExtensionName() {
        return extension.getName();
    }

    public String getWebsiteLink() {
        if (extension.getLinks().containsKey(LINK_TYPE.WEBSITE)) {
            return extension.getLinks().get(LINK_TYPE.WEBSITE);
        }
        return null;
    }

    public String getOpenLink() {
        if (extension.getLinks().containsKey(LINK_TYPE.OPEN)) {
            return extension.getLinks().get(LINK_TYPE.OPEN);
        }
        return null;
    }

    public String getExecutionLink() {
        return " ";
    }

    @Override
    public DTO_TYPE getDtoType() {
        return DTO_TYPE.INSTANCE;
    }

    @Override
    public String getOriginalName() {
        return "Instance";
    }
}
