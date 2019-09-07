/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.service.impl;

import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.Sale;
import com.totalit.smarthealth.domain.util.SaleStatus;
import com.totalit.smarthealth.repository.SaleRepository;
import com.totalit.smarthealth.service.SaleService;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.util.AppUtil;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SaleServiceImpl implements SaleService{
    final Logger logger = LoggerFactory.getLogger(SaleServiceImpl.class);
    @Autowired
    private SaleRepository repo;
    
    @Resource
    private UserService userService;
    
     
    private final MongoTemplate mongoTemplate;
    @Autowired
    public SaleServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
     
     
    @Override
    public List<Sale> getByCompany(Company company) {
       return repo.findByActiveAndCompany(Boolean.TRUE, company);
    }

    @Override
    public List<Sale> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Sale get(String id) {
        return repo.findById(id).get();
    }

    @Override
    public void delete(Sale t) {
        repo.delete(t);
    }

    @Override
    public List<Sale> getPageable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Sale save(Sale t) {
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
    public Boolean checkDuplicate(Sale current, Sale old, Company company) {
               throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates. 
    }

    @Override
    public List<Sale> findByActiveAndDateModified(Boolean active, Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Sale> findByActiveAndDateCreated(Boolean active, Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Sale findByUuid(String uuid) {
        return repo.findByUuid(uuid);
    }

    @Override
    public Boolean checkDuplicate(Sale current, Sale old) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long countByCompany(Company company) {
        return repo.countByCompany(company);
    }

    @Override
    public List<Sale> findByCompanyAndDateCreatedAndSaleStatus(Company company, Date date, SaleStatus saleStatus) {
        return repo.findByCompanyAndSaleStatus(company, saleStatus);
    }

    @Override
    public Long countByCompanyAndDateCreatedAndSaleStatus(Company company, Date date, SaleStatus saleStatus) {
        return repo.countByCompanyAndSaleStatus(company, saleStatus);
    }

    @Override
    public List<Sale> findSaleByDateCreated(Date date, Company company) {
        Query query = new Query();
        query.addCriteria(Criteria.where("dateCreated").gt(date).lte(date));
        query.addCriteria(Criteria.where("company").is(company));
        query.fields().include("amountAfterDiscount");
        query.fields().include("dateCreated");
        return mongoTemplate.find(query, Sale.class);
   // return mongoTemplate.query(Sale.class).distinct("lastname").as(Double.class).all(); 
    }
}
