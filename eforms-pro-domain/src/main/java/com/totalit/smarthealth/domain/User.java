package com.totalit.smarthealth.domain;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.totalit.smarthealth.domain.util.Gender;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author roy
 */
@Document(collection="users")
@TypeAlias("user")
@Getter @Setter @NoArgsConstructor
public class User extends BaseCompany {
    private static final long serialVersionUID = 1L;
    private String password;
    private String firstName;
    private String lastName;
    private String userName;
    private Gender gender;
    private String mobilePhone;
    private String officePhone;
    private String street;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;
    private String image;
    private String resetToken;  
    @Transient
    private String displayName;   
    private Set<UserRole> userRoles = new HashSet<>();
    private Set<Permission> permissions = new HashSet<>();
    private String companyId;
    private String companyName;
    @DBRef
    private Branch branch;

    public String getCompanyId() {
        if(getCompany()!=null){
            return getCompany().getId();
        }
        return null;
    }

    public String getCompanyName() {
         if(getCompany()!=null){
            return getCompany().getName();
        }
        return null;
    }

    public String getDisplayName() {
        return firstName.toUpperCase() + " " + lastName.toUpperCase();
    }
    

    
}
