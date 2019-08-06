/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Roy
 */
//@Document
@Getter @Setter @NoArgsConstructor
abstract public class BaseEntity implements Serializable {

    @Id
    private String id;
    private String uuid;
    @JsonIgnore
    @DBRef
    private User createdBy;
    @JsonIgnore
    @DBRef
    private User modifiedBy;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateCreated;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateModified;
    @Version
    private Long version;
    private Boolean active = Boolean.TRUE;
    private Boolean deleted = Boolean.FALSE;
    @Transient
    private String createdById;
   

    
    public BaseEntity(String id) {
        this.id = id;
    }
  

    public BaseEntity(User createdBy, User modifiedBy, Date dateCreated, Date dateModified) {
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
    }
    
    public String getCreatedById() {
        if(getCreatedBy()!=null){
           return getCreatedBy().getId();
        }
        return createdById;
    }

}
