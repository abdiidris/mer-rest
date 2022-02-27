package com.ame.rest.extension.instance;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ame.rest.extension.Extension;
import com.ame.rest.user.writer.Writer;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Instance {

   public enum STATE {
      DELETED, PAUSED, OPEN, BROKEN
   }

   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Id
   @JsonIgnore
   private Long id;

   @ManyToOne
   private Extension extension;

   @ManyToOne
   private Writer writer;

   @JsonIgnore
   @Lob
   private String data;

   @JsonIgnore
   private UUID instanceKey;

   private int executeCounter;

   private STATE state;

   @CreationTimestamp
   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "create_date")
   private Date createDate;

   public Instance(Extension extension, Writer writer) {
      this.writer = writer;
      this.extension = extension;
      this.executeCounter = 0;
      this.instanceKey = java.util.UUID.randomUUID();
      this.state = Instance.STATE.OPEN;

      // get initial data for extension
      this.data = extension.getInitialData();
   }

}
