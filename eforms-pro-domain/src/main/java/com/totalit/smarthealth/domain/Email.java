/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author roy
 */
@Document(collection="emails")
@TypeAlias("email")
@Getter @Setter @NoArgsConstructor
public class Email  extends BaseCompany{
   private static final long serialVersionUID = 1L;
      private String host;
      private String port;
      private String accountName;
      private String email;
      private String password;
      private Boolean isEmailWorking = Boolean.FALSE;
      private Boolean isEmailActive = Boolean.FALSE;
      private String emailAccountTypes;
      @Transient
      private String confirmPassword;   
      @Transient
      private String[] types;
     

    public String[] getTypes() {
          String[] nill = {};
        if(emailAccountTypes!=null){
            return emailAccountTypes.split(",");
        }
        return nill;
    } 
}
