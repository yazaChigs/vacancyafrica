package com.totalit.smarthealth.service.impl;

import com.totalit.smarthealth.domain.UserRole;
import com.totalit.smarthealth.repository.UserRoleRepo;
import com.totalit.smarthealth.service.UserRoleService;
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
 * @author Roy
 */
@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleRepo userRoleRepo;
    @Resource
    private UserService userService;

    @Override
    public List<UserRole> getAll() {
        return userRoleRepo.findByActive(Boolean.TRUE);
    }

    

    @Override
    public void delete(UserRole t) {
        if (t.getId() == null) {
            throw new IllegalStateException("Item to be deleted is in an inconsistent state");
        }
        t.setActive(Boolean.FALSE);
        t.setDeleted(Boolean.TRUE);
        userRoleRepo.save(t);
    }

    @Override
    public List<UserRole> getPageable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserRole save(UserRole t) {
        if (t.getId() == null) {
           t.setCreatedBy(userService.getCurrentUser());
            t.setDateCreated(new Date());
            t.setUuid(AppUtil.generateUUID());
            return userRoleRepo.save(t);
        }
         if(t.getCreatedById()!=null){
               t.setCreatedBy(userService.get(t.getCreatedById()));
           }
        t.setModifiedBy(userService.getCurrentUser());
        t.setDateModified(new Date());
        return userRoleRepo.save(t);
    }

    @Override
    public UserRole getByName(String name) {
        return userRoleRepo.findByActiveAndName(Boolean.TRUE, name);
    }
  

    @Override
    public List<UserRole> findByActiveAndDateModified(Boolean active, Date date) {
         return userRoleRepo.findByActiveAndDateModified(active, date);
    }

    @Override
    public UserRole findByUuid(String uuid) {
        return userRoleRepo.findByUuid(uuid);
    }

    @Override
    public List<UserRole> findByActiveAndDateCreated(Boolean active, Date date) {
          return userRoleRepo.findByActiveAndDateCreated(active, date);  
    }

    @Override
    public UserRole get(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean checkDuplicate(UserRole current, UserRole old) {
        return userRoleRepo.existsByNameIgnoreCaseAndActive(current.getName(), Boolean.TRUE);
    }
}
