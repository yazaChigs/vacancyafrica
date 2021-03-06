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
@Document(collection="payment_types")
@TypeAlias("paymentType")
@Getter @Setter @NoArgsConstructor
public class PaymentType extends BaseNameCompany{
    private Double amount;  //On confirm payment, this property carries the value for payment chosen
    private String paymentNote;
}
