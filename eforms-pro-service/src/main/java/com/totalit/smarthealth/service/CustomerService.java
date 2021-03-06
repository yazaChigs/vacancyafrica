/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.service;

import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.Customer;
import java.util.List;

/**
 *
 * @author roy
 */
public interface CustomerService extends GenericCompanyService<Customer>{
    List<Customer> lastTenCustomers(Company company);
    List<Customer> findTopCustomers(Company company);
}
