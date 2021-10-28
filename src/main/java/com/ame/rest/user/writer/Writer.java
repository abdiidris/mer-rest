package com.ame.rest.user.writer;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.ame.rest.user.User;

import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("WRITER")
public class Writer extends User{

    public Writer(String email, String password) {
        super(email, password);
    }
    public Writer(){}
    @Override
    public String[] getDefaultRoles() {
        return new String[]{"ROLE_WRITER"};
    }


}
