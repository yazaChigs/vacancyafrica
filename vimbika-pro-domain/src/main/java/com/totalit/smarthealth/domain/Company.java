/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.domain;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author roy
 */
@Document(collection="companies")
@TypeAlias("company")
@Getter @Setter @NoArgsConstructor
public class Company  extends BaseEntity{
 
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String description;
    private String mobilePhone;
    private String officePhone;
    private String email;
    private String fax;
    private String street;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;
    private String website;
    private String logo;
    @DBRef
    private Set<Module> modules = new HashSet<>(); 
}
