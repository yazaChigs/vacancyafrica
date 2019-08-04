/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 *
 * @author roy
 */
@Getter
@Setter
@NoArgsConstructor
abstract public class BaseCompany extends BaseEntity {

    @JsonIgnore
    @DBRef
    private Company company;
}
