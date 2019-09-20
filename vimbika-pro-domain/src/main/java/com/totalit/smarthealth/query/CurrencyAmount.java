/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.query;

import com.totalit.smarthealth.domain.Currency;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author roy
 */
@Getter @Setter @NoArgsConstructor
public class CurrencyAmount implements Serializable{
    private static final long serialVersionUID = 1L;
    private Double amount = 0.0;
    private Currency currency;
}
