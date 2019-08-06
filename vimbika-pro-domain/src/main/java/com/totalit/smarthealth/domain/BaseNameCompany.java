/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.totalit.smarthealth.domain.util.BaseNameType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 *
 * @author roy
 */
@Getter @Setter @NoArgsConstructor
abstract public class BaseNameCompany extends BaseEntity{
    private String name;
    private String description;
    @Transient
    private BaseNameType type;
    @JsonIgnore
    @DBRef
    private Company company;
}
