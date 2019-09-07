/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.service;

import com.totalit.smarthealth.domain.PaymentReceived;
import java.util.List;

/**
 *
 * @author roy
 */
public interface PaymentReceivedService extends GenericCompanyService<PaymentReceived>{
    public List<PaymentReceived> saveAll(List<PaymentReceived> paymentsReceived);
}
