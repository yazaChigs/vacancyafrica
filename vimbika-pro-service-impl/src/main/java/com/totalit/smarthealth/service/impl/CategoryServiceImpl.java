/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.service.impl;

import com.totalit.smarthealth.domain.Category;
import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.repository.CategoryRepository;
import com.totalit.smarthealth.service.CategoryService;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.util.AppUtil;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author roy
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository repo;
    @Resource
    private UserService userService;

    @Override
    public Category getByNameAndCompany(String name, Company company) {
        return repo.findByNameAndCompanyAndActive(name, company, Boolean.TRUE);
    }

    @Override
    public Boolean checkDuplicate(Category current, Category old, Company company) {
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
    public List<Category> getAll(Company company) {
         return repo.findByCompanyAndActive(company, Boolean.TRUE);
    }

    @Override
    public List<Category> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Category get(String id) {
        return repo.findById(id).get();
    }

    @Override
    public void delete(Category t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Category> getPageable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Category save(Category t) {
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
    public Boolean checkDuplicate(Category current, Category old) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Category> findByActiveAndDateModified(Boolean active, Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Category> findByActiveAndDateCreated(Boolean active, Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Category findByUuid(String uuid) {
        return repo.findByUuid(uuid);
    }
}
