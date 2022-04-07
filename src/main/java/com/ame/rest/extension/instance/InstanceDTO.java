package com.ame.rest.extension.instance;

import java.sql.Date;
import java.util.List;
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
public class InstanceDTO extends DTO {

    @JsonIgnore
    Extension extension;
    @JsonIgnore
    Writer writer;
    @JsonIgnore
    private UUID instanceKey;

    String instanceName;

    Date createDate;

    Instance.STATE state;

    List<Copy> copies;

    public String getExtensionName() {
        return extension.getName();
    }

    public List<Copy>  getCopies() {
        return copies;
    }

    public String  getKey() {
        return this.getId()+"_"+instanceKey.toString();
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
