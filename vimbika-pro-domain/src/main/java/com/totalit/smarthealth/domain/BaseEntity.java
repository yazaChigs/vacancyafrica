/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author tasu
 */
@Document
@Getter @Setter @NoArgsConstructor
abstract public class BaseEntity implements Serializable {

    @Id
    private String id;
    private String uuid;
    @DBRef
    private User createdBy;
    @DBRef
    private User modifiedBy;
    private Date dateCreated;
    private Date dateModified;
    @Version
    private Long version;
    private Boolean active = Boolean.TRUE;
    private Boolean deleted = Boolean.FALSE;
    
   

    
    public BaseEntity(String id) {
        this.id = id;
    }
  

    public BaseEntity(User createdBy, User modifiedBy, Date dateCreated, Date dateModified) {
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
    }

}
