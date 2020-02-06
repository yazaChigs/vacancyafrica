
package com.totalit.smarthealth.service.impl;

import com.totalit.smarthealth.domain.Advert;
import com.totalit.smarthealth.domain.ApplicationForm;
import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.repository.AdvertRepo;
import com.totalit.smarthealth.repository.ApplicationFormRepo;
import com.totalit.smarthealth.service.AdvertService;
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
public class AdvertServiceImpl implements AdvertService {

    final Logger logger = LoggerFactory.getLogger(AdvertServiceImpl.class);

    @Autowired
    private AdvertRepo repo;


    @Override
    public List<Advert> getAll() {
        return repo.findByActive(Boolean.TRUE);
    }

    @Override
    public Advert get(String id) {
        return null;
    }

    @Override
    public void delete(Advert advert) {

    }

    @Override
    public List<Advert> getPageable() {
        return null;
    }

    @Override
    public Advert save(Advert t) {
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
    public Boolean checkDuplicate(Advert current, Advert old) {
        return null;
    }

    @Override
    public List<Advert> findByActiveAndDateModified(Boolean active, Date date) {
        return null;
    }

    @Override
    public List<Advert> findByActiveAndDateCreated(Boolean active, Date date) {
        return null;
    }

    @Override
    public Advert findByUuid(String uuid) {
        return null;
    }


    @Override
    public List<Advert> getByCompany(Company company) {
        return repo.findByActiveAndCompany(Boolean.TRUE,company);
    }

    @Override
    public Boolean checkDuplicate(Advert current, Advert old, Company company) {
        return null;
    }
}
