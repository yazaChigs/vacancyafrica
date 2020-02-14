/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.repository;

import com.totalit.smarthealth.domain.Company;
import org.springframework.stereotype.Repository;

/**
 *
 * @author roy
 */
@Repository
public interface CompanyRepository extends AbstractRepo<Company, String> {
     public Boolean existsByNameIgnoreCaseAndActive(String name, Boolean active);
     Long countByActive(Boolean active);
     Company findByNameAndActive(String name, Boolean Active);
}
