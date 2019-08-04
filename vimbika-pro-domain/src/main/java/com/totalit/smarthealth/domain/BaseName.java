package com.totalit.smarthealth.domain;

import com.totalit.smarthealth.domain.util.BaseNameType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

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
    @Transient
    private BaseNameType type;

    public BaseName(String name) {
        this.name = name;
    }

         

}
