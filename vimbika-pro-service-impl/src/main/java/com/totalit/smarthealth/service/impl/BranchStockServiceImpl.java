/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.service.impl;

import com.totalit.smarthealth.domain.Branch;
import com.totalit.smarthealth.domain.BranchStock;
import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.InventoryItem;
import com.totalit.smarthealth.repository.BranchStockRepository;
import com.totalit.smarthealth.service.BranchStockService;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.util.AppUtil;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author roy
 */
@Service
@Transactional
public class BranchStockServiceImpl implements BranchStockService{
     @Autowired
    private BranchStockRepository repo;
    @Resource
    private UserService userService;
    @Autowired private MongoTemplate mongoTemplate;

    @Override
    public List<BranchStock> findByBranchAndItem(Branch branch, InventoryItem item) {
        return repo.findByBranchAndItem(branch, item);
    }

    @Override
    public List<BranchStock> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BranchStock get(String id) {
        return repo.findById(id).get();
    }

    @Override
    public void delete(BranchStock t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BranchStock> getPageable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BranchStock save(BranchStock t) {
        if (t.getId() == null) {
           t.setCreatedBy(userService.getCurrentUser());
            t.setDateCreated(new Date());
            t.setUuid(AppUtil.generateUUID());
            return repo.save(t);
        }
           if(t.getCreatedById()!=null){
               t.setCreatedBy(userService.get(t.getCreatedById()));
           }
        t.setModifiedBy(userService.getCurrentUser());
        t.setDateModified(new Date());
        return repo.save(t);
    }

    @Override
    public Boolean checkDuplicate(BranchStock current, BranchStock old) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BranchStock> findByActiveAndDateModified(Boolean active, Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BranchStock> findByActiveAndDateCreated(Boolean active, Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BranchStock findByUuid(String uuid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BranchStock> getAll(String companyId) {
        return repo.findByCompanyId(companyId);
//         List<BranchStock> branchStocks = mongoTemplate.find(new Query(Criteria.where("compan.id").is(company.getId())), BranchStock.class);
//         return branchStocks;
    }

    @Override
    public BranchStock getBranchInventoryItem(Branch branch, InventoryItem item, Boolean active) {
        return repo.findTopByBranchAndItemAndActive(branch, item, active);
    }
}
