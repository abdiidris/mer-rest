package com.ame.rest.extension.instance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ame.rest.extension.instance.Instance.STATE;

import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Copy {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Instance instance;

    private String description;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date creationDate;

    private Instance.STATE state;

    @ElementCollection
    private List<Date> executions;

    public Copy(String description, Instance instance) {
        this.instance = instance; 
        this.description = description;
        this.state = STATE.OPEN;
        executions = new ArrayList<>();
    }
}
