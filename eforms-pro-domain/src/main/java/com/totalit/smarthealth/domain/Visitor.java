/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.domain;

import com.totalit.smarthealth.domain.util.References;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author roy
 */
@Document(collection="Visitors")
@TypeAlias("Visitor")
@Getter @Setter @NoArgsConstructor
public class Visitor extends BaseEntity{
    private static final long serialVersionUID = 1L;
    private String firstName;
    private String middleName;
    private String lastName;
    private String password;
    private String userType;
    private String userName;
    private String street;
    private Date dateOfBirth;
    private String city;
    private String gender;
    private String stateProvince;
    private String idNumber;
    private String experience;
    private List<Category> jobCategory;
    private String aboutVisitor;
    private String postalCode;
    private List<References> references;
    private List<Skill> skills;
    private List<Certificate> certificates;
    private String country;
    private String email;
    private String mobilePhone;
    private String officePhone;
    private String website;
    private String logo;
    private List<String> uploads;
    private Boolean filesPublic;

    private static class Skill {
        String skill;

        public String getSkill() {
            return skill;
        }

        public void setSkill(String skill) {
            this.skill = skill;
        }
    }

    private static class Certificate {
        String certificate;

        public String getCertificate() {
            return certificate;
        }

        public void setCertificate(String certificate) {
            this.certificate = certificate;
        }
    }
}
