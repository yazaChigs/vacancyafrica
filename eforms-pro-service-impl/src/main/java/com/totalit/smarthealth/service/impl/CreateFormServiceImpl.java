
package com.totalit.smarthealth.service.impl;

import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.CreateForm;
import com.totalit.smarthealth.domain.User;
import com.totalit.smarthealth.repository.CreateFormRepo;
import com.totalit.smarthealth.repository.UserRepo;
import com.totalit.smarthealth.service.CreateFormService;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.util.AppUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
public class CreateFormServiceImpl implements CreateFormService {

    final Logger logger = LoggerFactory.getLogger(CreateFormServiceImpl.class);

    @Autowired
    private CreateFormRepo repo;
    @Resource
    private UserService userService;


    @Override
    public CreateForm findByFormName(String formName) {
        return repo.findByActiveAndFormName(Boolean.TRUE,formName);
    }

    @Override
    public CreateForm findByCompanyAndFormName(Company company, String formName) {
        return repo.findByCompanyAndFormName(company,formName);
    }


    @Override
    public List<CreateForm> getAll() {
        return null;
    }

    @Override
    public CreateForm get(String id) {
        return repo.findById(id).get();
    }

    @Override
    public void delete(CreateForm createForm) {

    }

    @Override
    public List<CreateForm> getPageable() {
        return null;
    }

    @Override
    public CreateForm save(CreateForm t) {
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
    public Boolean checkDuplicate(CreateForm current, CreateForm old) {
        return null;
    }

    @Override
    public List<CreateForm> findByActiveAndDateModified(Boolean active, Date date) {
        return null;
    }

    @Override
    public List<CreateForm> findByActiveAndDateCreated(Boolean active, Date date) {
        return null;
    }

    @Override
    public CreateForm findByUuid(String uuid) {
        return null;
    }

    @Override
    public List<CreateForm> getByCompany(Company company) {
        return repo.findByActiveAndCompany(Boolean.TRUE,company);
    }

    @Override
    public Boolean checkDuplicate(CreateForm current, CreateForm old, Company company) {
        return null;
    }
}
