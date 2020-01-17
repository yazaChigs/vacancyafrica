/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.domain;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author roy
 */
@Getter @Setter @NoArgsConstructor
public class ProductImage implements Serializable{
   private String big;
   private String small;
   private String medium;
   private Boolean active;
   private String description;
   private String url;
   private String label;
}
