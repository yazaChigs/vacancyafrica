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
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 *
 * @author roy
 */
@Document(collection="Answers")
@TypeAlias("answers")
@Getter @Setter @NoArgsConstructor
public class AnswerForm extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String id;
//    private String formName;
//    private String companyName;
//    private String jobName;
//    private Date startDate;
//    private Date endDate;
    private Object answers;


    
}
