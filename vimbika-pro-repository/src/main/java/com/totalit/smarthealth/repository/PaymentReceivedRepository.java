/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.repository;

import com.totalit.smarthealth.domain.PaymentReceived;
import org.springframework.stereotype.Repository;

/**
 *
 * @author roy
 */
@Repository
public interface PaymentReceivedRepository extends AbstractCompanyRepository<PaymentReceived, String>{
    
}
