package com.ame.rest.user.developer;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.ame.rest.user.User;

import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("DEVELOPER")
@NoArgsConstructor
public class Developer extends User {
    public Developer(String email, String password) {
        super(email, password);
    }

    @Override
    public String[] getDefaultRoles() {
        return new String[]{"ROLE_DEVELOPER"};
    }

}
