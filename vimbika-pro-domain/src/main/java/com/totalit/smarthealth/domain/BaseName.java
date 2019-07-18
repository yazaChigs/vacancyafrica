package com.totalit.smarthealth.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tasu
 */

@Getter @Setter @NoArgsConstructor
abstract public class BaseName extends BaseEntity{
    
    private String name;
    private String description;
    

}
