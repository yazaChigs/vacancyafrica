/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.service;

import com.totalit.smarthealth.domain.Branch;
import com.totalit.smarthealth.domain.BranchStock;
import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.InventoryItem;
import java.util.List;

/**
 *
 * @author roy
 */
public interface BranchStockService extends GenericService<BranchStock>{
    BranchStock findByBranchAndItem(Branch branch, InventoryItem item);
    List<BranchStock> getAll(Company company);
}
