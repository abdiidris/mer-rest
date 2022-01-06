package com.ame.rest.util.dto;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public interface DTO {

    public enum DTO_TYPE {
        BROWSE_EXTENSION,
        DEVELOP_EXTENSION,
        INSTANCE
    }

    public DTO_TYPE getDtoType();
    public String getOriginalName();

}