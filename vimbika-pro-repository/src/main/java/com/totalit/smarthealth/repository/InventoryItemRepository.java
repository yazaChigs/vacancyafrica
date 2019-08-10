/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.repository;

import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.InventoryItem;
import org.springframework.stereotype.Repository;

/**
 *
 * @author roy
 */
@Repository
public interface InventoryItemRepository extends AbstractCompanyRepository<InventoryItem, String>{
    public Boolean existsByNameIgnoreCaseAndActiveAndCompany(String name, Boolean active, Company company);
}
