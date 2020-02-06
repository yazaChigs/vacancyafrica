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

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author roy
 */
@Document(collection="Visitor")
@TypeAlias("Visitor")
@Getter @Setter @NoArgsConstructor
public class Visitor extends BaseEntity{
    private static final long serialVersionUID = 1L;
    private String firstName;
    private String lastName;
    private String password;
//    private String checkPassword;
    private String userType;
//private Set<UserRole> userRoles = new HashSet<>();
    private String userName;
    private String street;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;
    private String email;
    private String mobilePhone;
    private String officePhone;
    private String website;
    private String taxNumber;
    private double amountSpent = 0.0;
}
