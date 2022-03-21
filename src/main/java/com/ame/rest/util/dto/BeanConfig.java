package com.ame.rest.util.dto;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    // Used to create DTO objects for controllers
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
