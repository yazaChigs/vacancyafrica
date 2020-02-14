/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.service.impl;

import com.totalit.smarthealth.domain.Visitor;
import com.totalit.smarthealth.repository.VisitorRepository;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.service.VisitorService;
import com.totalit.smarthealth.util.AppUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author roy
 */
@Service
@Transactional
public class VisitorServiceImpl implements VisitorService {

    final Logger logger = LoggerFactory.getLogger(VisitorServiceImpl.class);
    @Autowired
    private VisitorRepository repo;

    @Resource
    private UserService userService;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public VisitorServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public List<Visitor> getAll() {
        return repo.findByActive(Boolean.TRUE);
    }

    @Override
    public Visitor get(String id) {
        return repo.findById(id).get();
    }

    @Override
    public void delete(Visitor t) {
        if (t.getId() == null) {
            throw new IllegalStateException("Item to be deleted is in an inconsistent state");
        }
        t.setActive(Boolean.FALSE);
        repo.save(t);
    }

    @Override
    public List<Visitor> getPageable() {
        return null;
    }

    @Override
    public Visitor save(Visitor t) {

        if (t.getId() == null) {
//            t.setCreatedBy(getCurrentUser());
            t.setDateCreated(new Date());
            t.setUuid(AppUtil.generateUUID());
            t.setPassword(encryptPassword(t.getPassword()));
            return repo.save(t);
        }
        if(t.getCreatedById()!=null){
//            t.setCreatedBy(get(t.getCreatedById()));
        }
        Visitor user = get(t.getId());
        if(!user.getPassword().equalsIgnoreCase(t.getPassword())){
            t.setPassword(encryptPassword(t.getPassword()));
        }
//        t.setModifiedBy(getCurrentUser());
        t.setDateModified(new Date());
        return repo.save(t);
    }
    private String encryptPassword(String pass){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(pass);
        return hashedPassword;
    }

    @Override
    public Boolean checkDuplicate(Visitor current, Visitor old) {
        if (current.getId() == null) {
            return repo.existsByActiveAndUserNameIgnoreCase(Boolean.TRUE, current.getUserName());
        }
        old = get(current.getId());
        if (!current.getUserName().equalsIgnoreCase(old.getUserName())) {
            return repo.existsByActiveAndUserNameIgnoreCase(Boolean.TRUE, current.getUserName());
        } else {
            return Boolean.FALSE;
        }

    }

    @Override
    public List<Visitor> findByActiveAndDateModified(Boolean active, Date date) {
        return repo.findByActiveAndDateModified(Boolean.TRUE, date);
    }

    @Override
    public List<Visitor> findByActiveAndDateCreated(Boolean active, Date date) {
        return repo.findByActiveAndDateModified(Boolean.TRUE, date);
    }

    @Override
    public Visitor findByUuid(String uuid) {
        return repo.findByUuid(uuid);
    }

    @Override
    public Visitor findByUserName(String name) {
        return repo.findByActiveAndUserName(Boolean.TRUE,name);
    }

    @Override
    public String getCurrentUsername() {
        String username = getCurrentUsername();
        if (username == null) {
            return null;
        }
        Visitor user = findByUserName(username);
        if (user == null) {
            return null;
        }
        return user.getUserName();
    }

    @Override
    public Visitor getCurrentUser() {
        String username = getCurrentUsername();
        if (username == null) {
            return null;
        }
        Visitor visitor = findByUserName(username);
        if (visitor == null) {
            return null;
        }
        return visitor;
    }

    @Override
    public List<Visitor> getAllInActive() {
        return null;
    }

    @Override
    public Visitor changePassword(Visitor t) {
        return null;
    }

    @Override
    public Visitor findByUserNameAndPasswordAndActive(String userName, String password, Boolean active) {
        return repo.findByUserNameAndPasswordAndActive(userName, password,Boolean.TRUE);
    }

    @Override
    public Optional<Visitor> findUserByResetToken(String resetToken) {
        return Optional.empty();
    }
}
