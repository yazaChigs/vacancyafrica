/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.repository;

import com.totalit.smarthealth.domain.Brand;
import com.totalit.smarthealth.domain.Category;
import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.InventoryItem;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author roy
 */
@Repository
public interface InventoryItemRepository extends AbstractCompanyRepository<InventoryItem, String>{
    public Boolean existsByNameIgnoreCaseAndActiveAndCompany(String name, Boolean active, Company company);
    List<InventoryItem> findByCompanyAndActiveAndNameLike(Company company, Boolean active, String name);
    List<InventoryItem> findByCompanyAndActiveAndItemCodeLike(Company company, Boolean active, String itemCode);
    List<InventoryItem> findByCompanyAndActiveAndCategory(Company company, Boolean active, Category category);
    List<InventoryItem> findByCompanyAndActiveAndBrand(Company company, Boolean active, Brand brand);
}
