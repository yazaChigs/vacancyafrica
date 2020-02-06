
package com.totalit.smarthealth.service.impl;

import com.totalit.smarthealth.domain.ApplicationForm;
import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.repository.ApplicationFormRepo;
import com.totalit.smarthealth.service.ApplicationFormService;
import com.totalit.smarthealth.util.AppUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 *
 * @author roy
 */
@Service
@Transactional
public class ApplicationFormServiceImpl implements ApplicationFormService {

    final Logger logger = LoggerFactory.getLogger(ApplicationFormServiceImpl.class);

    @Autowired
    private ApplicationFormRepo repo;


    @Override
    public List<ApplicationForm> getAll() {
        return null;
    }

    @Override
    public ApplicationForm get(String id) {
        return repo.findById(id).get();
    }

    @Override
    public void delete(ApplicationForm answerForm) {

    }

    @Override
    public List<ApplicationForm> getPageable() {
        return null;
    }

    @Override
    public ApplicationForm save(ApplicationForm t) {
        if (null == t.getId()) {
//            t.setCreatedBy(getCurrentUser());
            t.setDateCreated(new Date());
            t.setUuid(AppUtil.generateUUID());
//            t.setPassword(encryptPassword(t.getPassword()));
            return repo.save(t);
        }
//          if(t.getCreatedById()!=null){
//               t.setCreatedBy(get(t.getCreatedById()));
//           }
//        t.setModifiedBy(getCurrentUser());
        t.setDateModified(new Date());
        return repo.save(t);
    }

    @Override
    public Boolean checkDuplicate(ApplicationForm current, ApplicationForm old) {
        return null;
    }

    @Override
    public List<ApplicationForm> findByActiveAndDateModified(Boolean active, Date date) {
        return null;
    }

    @Override
    public List<ApplicationForm> findByActiveAndDateCreated(Boolean active, Date date) {
        return null;
    }

    @Override
    public ApplicationForm findByUuid(String uuid) {
        return null;
    }


    @Override
    public List<ApplicationForm> getByCompany(Company company) {
        return repo.findByActiveAndCompany(Boolean.TRUE,company);
    }

    @Override
    public Boolean checkDuplicate(ApplicationForm current, ApplicationForm old, Company company) {
        return null;
    }
}
