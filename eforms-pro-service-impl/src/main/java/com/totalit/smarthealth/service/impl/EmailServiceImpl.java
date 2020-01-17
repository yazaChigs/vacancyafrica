/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.service.impl;

import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.Email;
import com.totalit.smarthealth.repository.EmailRepository;
import com.totalit.smarthealth.service.EmailService;
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
public class EmailServiceImpl implements EmailService{
    
    final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    @Autowired
    private EmailRepository repo;
    
     @Resource
    private UserService userService;
    @Override
    public List<Email> getByCompany(Company company) {
         return repo.findByActiveAndCompany(Boolean.TRUE, company);
    }

    @Override
    public Boolean checkDuplicate(Email current, Email old, Company company) {
        if(current.getId() == null){
        return repo.existsByEmailIgnoreCaseAndActiveAndCompany(current.getEmail(), Boolean.TRUE, company);
        }
        old = get(current.getId());
        if(!current.getEmail().equalsIgnoreCase(old.getEmail())){
             return repo.existsByEmailIgnoreCaseAndActiveAndCompany(current.getEmail(), Boolean.TRUE, company);
        }else{
           return Boolean.FALSE;
        }
    }

    @Override
    public List<Email> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Email get(String id) {
        return repo.findById(id).get();
    }

    @Override
    public void delete(Email t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Email> getPageable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Email save(Email t) {
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
    public Boolean checkDuplicate(Email current, Email old) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Email> findByActiveAndDateModified(Boolean active, Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Email> findByActiveAndDateCreated(Boolean active, Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Email findByUuid(String uuid) {
        return repo.findByUuid(uuid);
    }
}
