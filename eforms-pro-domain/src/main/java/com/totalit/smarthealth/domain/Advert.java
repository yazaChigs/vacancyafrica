/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 *
 * @author roy
 */
@Document(collection="Advert")
@TypeAlias("advert")
@Getter @Setter @NoArgsConstructor
public class Advert extends BaseCompany{
    private String title;
    private String job;
    @DBRef
    private List<Category> category;
    private String formName;
    private String description;
    private Date startDate;
    private Date endDate;
    private File files;
}
