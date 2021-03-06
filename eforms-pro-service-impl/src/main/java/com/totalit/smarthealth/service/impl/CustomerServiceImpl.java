/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.service.impl;

import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.Customer;
import com.totalit.smarthealth.domain.InventoryItem;
import com.totalit.smarthealth.repository.CustomerRepository;
import com.totalit.smarthealth.service.CustomerService;
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
public class CustomerServiceImpl implements CustomerService{

    final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    @Autowired
    private CustomerRepository repo;
    
    @Resource
    private UserService userService;
    
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CustomerServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    
    
    @Override
    public List<Customer> getByCompany(Company company) {
         return repo.findByActiveAndCompany(Boolean.TRUE, company);
    }

    @Override
    public Boolean checkDuplicate(Customer current, Customer old, Company company) {
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
    public List<Customer> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Customer get(String id) {
        return repo.findById(id).get();
    }

    @Override
    public void delete(Customer t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Customer> getPageable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Customer save(Customer t) {
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
    public Boolean checkDuplicate(Customer current, Customer old) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Customer> findByActiveAndDateModified(Boolean active, Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Customer> findByActiveAndDateCreated(Boolean active, Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Customer findByUuid(String uuid) {
        return repo.findByUuid(uuid);
    }

    @Override
    public List<Customer> lastTenCustomers(Company company) {
       // Pageable pageableRequest = PageRequest.of(0, 10, Sort.Direction.DESC);
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "dateCreated"));
        query.limit(10);
        //query.with(pageableRequest);
        query.addCriteria(Criteria.where("company").is(company));
        query.addCriteria(Criteria.where("active").is(true));
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public List<Customer> findTopCustomers(Company company) {
         Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "amountSpent"));
        query.limit(10);
        query.addCriteria(Criteria.where("company").is(company));
        query.addCriteria(Criteria.where("active").is(true));
        return mongoTemplate.find(query, Customer.class);
    }
    
}
