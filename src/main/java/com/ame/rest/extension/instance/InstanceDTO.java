package com.ame.rest.extension.instance;

import java.sql.Date;
import java.util.UUID;

import com.ame.rest.extension.Extension;
import com.ame.rest.extension.Extension.LINK_TYPE;
import com.ame.rest.user.writer.Writer;
import com.ame.rest.util.dto.DTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstanceDTO implements DTO {

    @JsonIgnore
    Long id;
    @JsonIgnore
    Extension extension;
    @JsonIgnore
    Writer writer;
    @JsonIgnore
    private UUID instanceKey;

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

    public String getKey() {
        return this.id+"_"+this.instanceKey.toString();
    }

    public String getOpenLink() {
        if (extension.getLinks().containsKey(LINK_TYPE.OPEN)) {
            return extension.getLinks().get(LINK_TYPE.OPEN);
        }
        return null;
    }

    public String getExecutionLink() {
        return "<iframe frameBorder='0'  width='100%' height='100%' src='http://localhost:8080/instance/run/" + id + "'/>";
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
