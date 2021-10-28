package com.ame.rest.extension;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Extension {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;
    String name;
    String description;
    String restUrl;
    String website;

    public Extension() {
    }

    public Extension(Long id, String name, String description, String restUrl, String website) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.restUrl = restUrl;
        this.website = website;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRestUrl() {
        return this.restUrl;
    }

    public void setRestUrl(String restUrl) {
        this.restUrl = restUrl;
    }

    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Extension id(Long id) {
        setId(id);
        return this;
    }

    public Extension name(String name) {
        setName(name);
        return this;
    }

    public Extension description(String description) {
        setDescription(description);
        return this;
    }

    public Extension restUrl(String restUrl) {
        setRestUrl(restUrl);
        return this;
    }

    public Extension website(String website) {
        setWebsite(website);
        return this;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", restUrl='" + getRestUrl() + "'" +
            ", website='" + getWebsite() + "'" +
            "}";
    }

}
