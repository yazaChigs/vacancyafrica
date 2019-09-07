/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.service;

import com.totalit.smarthealth.domain.Payment;
import java.util.List;

/**
 *
 * @author roy
 */
public interface PaymentService extends GenericCompanyService<Payment>{
    public List<Payment> saveAll(List<Payment> payments);
}
