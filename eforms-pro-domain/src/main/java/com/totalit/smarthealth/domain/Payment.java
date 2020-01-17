/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.totalit.smarthealth.domain.util.PaymentDescription;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author roy
 */
@Document(collection = "payments")
@TypeAlias("payment")
@Getter
@Setter
@NoArgsConstructor
public class Payment extends BaseCompany{
    @JsonIgnoreProperties({ "active", "deleted", "createdById", "uuid", "version", "dateCreated", "dateModified"})
    @DBRef
    private PaymentType paymentType;
    private Double amount;
    private String paymentNote;
    @CreatedDate
    private LocalDateTime dateTime;
    private PaymentDescription paymentDescription;
    @JsonIgnoreProperties({ "active", "deleted", "createdById", "uuid", "version", "dateCreated", "dateModified"})
    @DBRef
    private Currency currency;
}
