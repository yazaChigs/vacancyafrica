package com.totalit.smarthealth.domain;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 *
 * @author roy
 */
@Document(collection="ApplicationForm")
@TypeAlias("applicationForm")
@Getter @Setter @NoArgsConstructor
public class ApplicationForm extends BaseCompany {
    private static final long serialVersionUID = 1L;
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private Date dateOfBirth;
    private String mobile;
    private String email;
    private String address;
    private Integer overallWeight;
    private String formName;
    private String jobName;
    private Date startDate;
    private Date endDate;
    @DBRef
    private List<Category> Category;
    private Object answers;


    
}
