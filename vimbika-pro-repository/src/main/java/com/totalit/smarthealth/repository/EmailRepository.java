/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.repository;

import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.Email;
import org.springframework.stereotype.Repository;

/**
 *
 * @author roy
 */
@Repository
public interface EmailRepository extends AbstractCompanyRepository<Email, String>{
    public Boolean existsByEmailIgnoreCaseAndActiveAndCompany(String name, Boolean active, Company company);
}
