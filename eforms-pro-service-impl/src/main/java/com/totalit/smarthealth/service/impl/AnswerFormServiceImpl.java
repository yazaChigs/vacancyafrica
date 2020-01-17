
package com.totalit.smarthealth.service.impl;

import com.totalit.smarthealth.domain.AnswerForm;
import com.totalit.smarthealth.domain.CreateForm;
import com.totalit.smarthealth.repository.AnswerFormRepo;
import com.totalit.smarthealth.repository.CreateFormRepo;
import com.totalit.smarthealth.service.AnswerFormService;
import com.totalit.smarthealth.service.CreateFormService;
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
public class AnswerFormServiceImpl implements AnswerFormService {

    final Logger logger = LoggerFactory.getLogger(AnswerFormServiceImpl.class);

    @Autowired
    private AnswerFormRepo answerFormRepo;


    @Override
    public List<AnswerForm> getAll() {
        return null;
    }

    @Override
    public AnswerForm get(String id) {
        return null;
    }

    @Override
    public void delete(AnswerForm answerForm) {

    }

    @Override
    public List<AnswerForm> getPageable() {
        return null;
    }

    @Override
    public AnswerForm save(AnswerForm t) {
        if (null == t.getId()) {
//            t.setCreatedBy(getCurrentUser());
            t.setDateCreated(new Date());
            t.setUuid(AppUtil.generateUUID());
//            t.setPassword(encryptPassword(t.getPassword()));
            return answerFormRepo.save(t);
        }
//          if(t.getCreatedById()!=null){
//               t.setCreatedBy(get(t.getCreatedById()));
//           }
//        t.setModifiedBy(getCurrentUser());
        t.setDateModified(new Date());
        return answerFormRepo.save(t);
    }

    @Override
    public Boolean checkDuplicate(AnswerForm current, AnswerForm old) {
        return null;
    }

    @Override
    public List<AnswerForm> findByActiveAndDateModified(Boolean active, Date date) {
        return null;
    }

    @Override
    public List<AnswerForm> findByActiveAndDateCreated(Boolean active, Date date) {
        return null;
    }

    @Override
    public AnswerForm findByUuid(String uuid) {
        return null;
    }
}
