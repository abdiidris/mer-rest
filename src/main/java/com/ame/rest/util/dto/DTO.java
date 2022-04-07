package com.ame.rest.util.dto;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DTO {

    public enum DTO_TYPE {
        BROWSE_EXTENSION,
        DEVELOP_EXTENSION,
        INSTANCE,
        USER
    }

    Long id;

    Map<String, Object> additional;

    public abstract DTO_TYPE getDtoType();

    public abstract String getOriginalName();

}