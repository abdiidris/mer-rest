package com.ame.rest.user.writer;

import java.util.List;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.ame.rest.extension.instance.Instance;
import com.ame.rest.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter
@Getter
@DiscriminatorValue("WRITER")
public class Writer extends User{

    @OneToMany(mappedBy = "writer")
    @JsonIgnore
    List<Instance> instances;

    public Writer(String email, String password) {
        super(email, password);
    }

    @Override
    public String[] getDefaultRoles() {
        return new String[]{"ROLE_WRITER"};
    }


}
