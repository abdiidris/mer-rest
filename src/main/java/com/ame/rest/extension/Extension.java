package com.ame.rest.extension;

import java.util.List;
import java.util.Map;
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
@Getter
@Setter
public class Extension {

    public static enum LINK_TYPE {
        EDIT, EXECUTE, DELETE, WEBSITE
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

    // this is the value that the data for instance is initially set to
    String initialData;

    // this means the initial data will be appended at the end of execute link
    boolean urlExtension;

    String dataTip;

    protected Extension(String name, String description, Map<LINK_TYPE, String> links) {
        this.name = name;
        this.description = description;
        this.links = links;
        this.initialData = "";
    }

    public Extension(String name, String description, Map<LINK_TYPE,String> links, Developer developer, List<Instance> instances, String initialData, boolean urlExtension, String dataTip) {
 
        this.name = name;
        this.description = description;
        this.links = links;
        this.developer = developer;
        this.instances = instances;
        this.initialData = initialData;
        this.urlExtension = urlExtension;
        this.dataTip = dataTip;
    }

}
