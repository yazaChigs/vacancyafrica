/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.service.impl;

import com.totalit.smarthealth.domain.Brand;
import com.totalit.smarthealth.domain.Category;
import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.InventoryItem;
import com.totalit.smarthealth.repository.InventoryItemRepository;
import com.totalit.smarthealth.service.InventoryItemService;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.util.AppUtil;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author roy
 */
@Service
@Transactional
public class InventoryItemServiceImpl implements InventoryItemService{

     final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    @Autowired
    private InventoryItemRepository repo;
    private final MongoTemplate mongoTemplate;
    
     @Resource
    private UserService userService;
     
     @Autowired
    public InventoryItemServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    @Override
    public List<InventoryItem> getByCompany(Company company) {
       return repo.findByActiveAndCompanyOrderByDateCreatedDesc(Boolean.TRUE, company);
    }

    @Override
    public Boolean checkDuplicate(InventoryItem current, InventoryItem old, Company company) {
        if(current.getId() == null){
        return repo.existsByNameIgnoreCaseAndActiveAndCompany(current.getName(), Boolean.TRUE, company);
        }
        old = get(current.getId());
        if(!current.getName().equalsIgnoreCase(old.getName())){
             return repo.existsByNameIgnoreCaseAndActiveAndCompany(current.getName(), Boolean.TRUE, company);
        }else{
           return Boolean.FALSE;
        }
    }

    @Override
    public List<InventoryItem> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public InventoryItem get(String id) {
        return repo.findById(id).get();
    }

    @Override
    public void delete(InventoryItem t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<InventoryItem> getPageable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public InventoryItem save(InventoryItem t) {
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
    public Boolean checkDuplicate(InventoryItem current, InventoryItem old) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<InventoryItem> findByActiveAndDateModified(Boolean active, Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<InventoryItem> findByActiveAndDateCreated(Boolean active, Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public InventoryItem findByUuid(String uuid) {
         return repo.findByUuid(uuid);
    }

    @Override
    public List<InventoryItem> findByCompanyAndActiveAndNameLike(Company company, Boolean active, String name) {
        return repo.findByCompanyAndActiveAndNameLike(company, active, name);
    }

    @Override
    public List<InventoryItem> findByCompanyAndActiveAndItemCodeLike(Company company, Boolean active, String itemCode) {
        return repo.findByCompanyAndActiveAndItemCodeLike(company, active, itemCode);
    }

    @Override
    public List<InventoryItem> findByCompanyAndActiveAndCategory(Company company, Boolean active, Category category) {
        return repo.findByCompanyAndActiveAndCategory(company, active, category);
    }

    @Override
    public List<InventoryItem> findByCompanyAndActiveAndBrand(Company company, Boolean active, Brand brand) {
        return repo.findByCompanyAndActiveAndBrand(company, active, brand);
    }

    @Override
    public List<InventoryItem> findMostSoldItems(Company company) {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "saleCount"));
        query.limit(10);
        //query.with(pageableRequest);
        query.addCriteria(Criteria.where("company").is(company));
        query.addCriteria(Criteria.where("active").is(true));
        return mongoTemplate.find(query, InventoryItem.class);
    }
    
}
