package com.ame.rest.user;

import com.ame.rest.util.dto.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter @Setter
public class UserDto extends DTO{

    private String  email;
    private String[] roles;

    @Override
    public DTO_TYPE getDtoType() {
        return DTO_TYPE.USER;
    }

    @Override
    public String getOriginalName() {
        return "User";
    }

}
