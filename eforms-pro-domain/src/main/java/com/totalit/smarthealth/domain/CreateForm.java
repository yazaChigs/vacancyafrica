package com.totalit.smarthealth.domain;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.totalit.smarthealth.domain.util.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author roy
 */
@Document(collection="CreateForm")
@TypeAlias("createform")
@Getter @Setter @NoArgsConstructor
public class CreateForm extends BaseCompany {
    private static final long serialVersionUID = 1L;
    private String id;
    private String formName;
    private String companyName;
    private String companyId;
    private Integer overallWeight;
    private List<Category> category;
    private String jobName;
    private Date startDate;
    private Date endDate;
    private List<Object> questions;


    
}
