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

/**
 *
 * @author roy
 */
@Document(collection="roles")
@TypeAlias("role")
@Getter @Setter @NoArgsConstructor
public class UserRole extends BaseName{


    public UserRole(String name) {
        super(name);
    }
}
