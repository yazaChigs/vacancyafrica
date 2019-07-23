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
@Document(collection="modules")
@TypeAlias("module")
@Getter @Setter @NoArgsConstructor
public class Module extends BaseName{
    
}
