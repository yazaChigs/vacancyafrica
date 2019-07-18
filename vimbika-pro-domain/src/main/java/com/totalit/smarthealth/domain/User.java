package com.totalit.smarthealth.domain;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.totalit.smarthealth.domain.util.Gender;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author tasu
 */
@Document(collection="users")
@TypeAlias("user")
@Getter @Setter @NoArgsConstructor
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String password;
    @Transient
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String middleName;
    private Date dob;
    private String userName;
    private Gender gender;
    private String nationalId;
    private String phoneHome;
    private String phoneCell;
    private String phoneBusiness;
    private String address;
    private String country;
     private String resetToken;
   
    @Transient
    private String displayName;
    @Transient
    private String dateOfBirth;
    
    private Set<UserRole> userRoles = new HashSet<>();

  
   
   
}
