package com.ame.rest.user;

import javax.persistence.Entity;

import com.ame.rest.user.developer.Developer;
import com.ame.rest.user.writer.Writer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;


@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
  @Type(value = Writer.class, name = "writer"),
  @Type(value = Developer.class, name = "developer")
})


@Entity
@NoArgsConstructor @Getter @Setter
@ToString
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="DISCRIMINATOR")
@EqualsAndHashCode
public abstract class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id @JsonIgnore
    private Long id;

    @NonNull
    @Column(name="email", unique=true)
    private String  email;

    @NonNull
    @ToString.Exclude
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String password;

    @JsonIgnore
    private String[] roles;

    @JsonIgnore
    private boolean verified;

    protected User(String email, String password) {
        this.email = email;
        verified = false;
        this.password = password;
    }

    @JsonProperty
    public void setPassword(String password) {
		this.password = password;
	}

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @JsonIgnore
    abstract public String[] getDefaultRoles();
}
