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
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author roy
 */
@Document(collection="suppliers")
@TypeAlias("supplier")
@Getter @Setter @NoArgsConstructor
public class Supplier extends BaseCompany{
    private static final long serialVersionUID = 1L;    
    private String name;
    private String description;
    private String supplierContactPerson;
    private String street;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String bpNumber;
    private String country;
    private String email;
    private String mobilePhone;
    private String officePhone;
    private String website;
    private String vatNumber;
    
    
}
