package com.ame.rest.extension;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.ame.rest.extension.instance.Instance;
import com.ame.rest.user.developer.Developer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Extension {

    public enum LINK_TYPE {
        OPEN, EXECUTE, DELETE, WEBSITE
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;
    @Column(nullable = false, unique = true)
    String name;
    String description;

    @ElementCollection
    Map<LINK_TYPE, String> links;

    @ManyToOne
    Developer developer;

    @OneToMany(mappedBy = "extension")
    List<Instance> instances;

    protected Extension(Long id, String name, String description, Map<LINK_TYPE, String> links) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.links = links;
    }
}
