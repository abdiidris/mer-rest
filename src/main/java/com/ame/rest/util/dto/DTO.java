package com.ame.rest.util.dto;


public interface DTO {

    public enum DTO_TYPE {
        BROWSE_EXTENSION,
        DEVELOP_EXTENSION,
        INSTANCE
    }

    public DTO_TYPE getDtoType();
    public String getOriginalName();

}