/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.repository;

import com.totalit.smarthealth.domain.Branch;
import com.totalit.smarthealth.domain.BranchStock;
import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.InventoryItem;
import java.util.List;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author roy
 */
@Repository
public interface BranchStockRepository extends AbstractRepo<BranchStock, String> {
    List<BranchStock> findByBranchAndItem(Branch branch, InventoryItem item);
    List<BranchStock> findByCompanyId(String id);
    
}
