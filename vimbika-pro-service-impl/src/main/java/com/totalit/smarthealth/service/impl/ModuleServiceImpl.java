/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.service.impl;

import com.totalit.smarthealth.domain.Module;
import com.totalit.smarthealth.repository.ModuleRepository;
import com.totalit.smarthealth.service.ModuleService;
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
public class ModuleServiceImpl  implements ModuleService {

    @Autowired
    private ModuleRepository repo;
    @Resource
    private UserService userService;
    
    @Override
    public Module getByName(String name) {
        return repo.findByActiveAndName(Boolean.TRUE, name);
    }

    @Override
    public List<Module> getAll() {
        return repo.findByActive(Boolean.TRUE);
    }

    @Override
    public Module get(String id) {
        return repo.findById(id).get();
    }

    @Override
    public void delete(Module t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Module> getPageable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Module save(Module t) {
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
    public Boolean checkDuplicate(Module current, Module old) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Module> findByActiveAndDateModified(Boolean active, Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Module> findByActiveAndDateCreated(Boolean active, Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Module findByUuid(String uuid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
