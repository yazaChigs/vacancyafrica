/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.service.impl;

import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.Payment;
import com.totalit.smarthealth.repository.PaymentRepository;
import com.totalit.smarthealth.service.PaymentService;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.util.AppUtil;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author roy
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService{
    final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
    @Autowired
    private PaymentRepository repo;
    
     @Resource
    private UserService userService;
    
    @Override
    public List<Payment> getByCompany(Company company) {
       return repo.findByActiveAndCompany(Boolean.TRUE, company);
    }

    @Override
    public List<Payment> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Payment get(String id) {
        return repo.findById(id).get();
    }

    @Override
    public void delete(Payment t) {
        repo.delete(t);
    }

    @Override
    public List<Payment> getPageable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Payment save(Payment t) {
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
    public Boolean checkDuplicate(Payment current, Payment old, Company company) {
       return false;
    }

    @Override
    public List<Payment> findByActiveAndDateModified(Boolean active, Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Payment> findByActiveAndDateCreated(Boolean active, Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Payment findByUuid(String uuid) {
        return repo.findByUuid(uuid);
    }

    @Override
    public Boolean checkDuplicate(Payment current, Payment old) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Payment> saveAll(List<Payment> payments) {
        return repo.saveAll(payments);
    }
}
