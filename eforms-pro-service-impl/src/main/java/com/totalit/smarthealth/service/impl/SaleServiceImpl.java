/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.service.impl;

import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.InventoryItem;
import com.totalit.smarthealth.domain.Sale;
import com.totalit.smarthealth.domain.util.SaleStatus;
import com.totalit.smarthealth.query.SaleDynamicQuery;
import com.totalit.smarthealth.repository.SaleRepository;
import com.totalit.smarthealth.service.InventoryItemService;
import com.totalit.smarthealth.service.SaleService;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.util.AppUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author roy
 */

@Service
@Transactional
public class SaleServiceImpl implements SaleService{
    final Logger logger = LoggerFactory.getLogger(SaleServiceImpl.class);
    @Autowired
    private SaleRepository repo;
    
    @Autowired
    private InventoryItemService inventoryItemService;
    
    @Resource
    private UserService userService;
    
     
    private final MongoTemplate mongoTemplate;
    @Autowired
    public SaleServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
     
     
    @Override
    public List<Sale> getByCompany(Company company) {
       return repo.findByActiveAndCompanyOrderByDateCreatedDesc(Boolean.TRUE, company);
    }

    @Override
    public List<Sale> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Sale get(String id) {
        return repo.findById(id).get();
    }

    @Override
    public void delete(Sale t) {
        repo.delete(t);
    }

    @Override
    public List<Sale> getPageable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Sale save(Sale t) {
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
    public Boolean checkDuplicate(Sale current, Sale old, Company company) {
               throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates. 
    }

    @Override
    public List<Sale> findByActiveAndDateModified(Boolean active, Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Sale> findByActiveAndDateCreated(Boolean active, Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Sale findByUuid(String uuid) {
        return repo.findByUuid(uuid);
    }

    @Override
    public Boolean checkDuplicate(Sale current, Sale old) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long countByCompany(Company company) {
        return repo.countByCompany(company);
    }

    @Override
    public List<Sale> findByCompanyAndDateCreatedAndSaleStatus(Company company, Date date, SaleStatus saleStatus) {
        return repo.findByCompanyAndSaleStatus(company, saleStatus);
    }

    @Override
    public Long countByCompanyAndDateCreatedAndSaleStatus(Company company, Date date, SaleStatus saleStatus) {
        return repo.countByCompanyAndSaleStatus(company, saleStatus);
    }

    @Override
    public List<Sale> findSaleByDateCreated(Date startDate, Company company) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE, +1);
        Date endDate = calendar.getTime();
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "dateCreated"));
        List<Criteria> criteria = new ArrayList<>();
        criteria.add(Criteria.where("company").is(company));
        criteria.add(Criteria.where("dateCreated").lte(endDate));
        criteria.add(Criteria.where("dateCreated").gte(startDate));
       
        criteria.add(Criteria.where("saleStatus").is(SaleStatus.COMPLETE));
        query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
        return mongoTemplate.find(query, Sale.class);
        
//        Query query = new Query();
//        query.addCriteria(Criteria.where("dateCreated").gt(date).lte(date));
//        query.addCriteria(Criteria.where("company").is(company));
//        query.fields().include("amountAfterDiscount");
//        query.fields().include("dateCreated");
//        return mongoTemplate.find(query, Sale.class);
   // return mongoTemplate.query(Sale.class).distinct("lastname").as(Double.class).all(); 
    }

    @Override
    public List<Sale> findBySearchSaleDto(SaleDynamicQuery saleDynamicQuery) {
         Query query = new Query();
         query.with(new Sort(Sort.Direction.DESC, "dateCreated"));
        List<Criteria> criteria = new ArrayList<>();
       // Pageable pageableRequest = PageRequest.;
        Date endDate = getEndDate(saleDynamicQuery);
        criteria.add(Criteria.where("company").is(saleDynamicQuery.getCompany()));
        if (saleDynamicQuery.getStartDate() != null) {
            criteria.add(Criteria.where("dateCreated").gte(saleDynamicQuery.getStartDate()));
        }
        if (endDate != null) {
            criteria.add(Criteria.where("dateCreated").lte(endDate));
        }
        if (saleDynamicQuery.getCustomer() != null) {
            criteria.add(Criteria.where("customer").is(saleDynamicQuery.getCustomer()));
        }
        if (saleDynamicQuery.getBrand() != null) {   
            criteria.add(Criteria.where("saleItems").elemMatch(Criteria.where("brand").is(saleDynamicQuery.getBrand())));
        }
        if (saleDynamicQuery.getCategory() != null) {
            criteria.add(Criteria.where("saleItems").elemMatch(Criteria.where("category").is(saleDynamicQuery.getCategory())));
        }
        if(saleDynamicQuery.getItemCode()!=null && !saleDynamicQuery.getItemCode().equalsIgnoreCase("")){
            criteria.add(Criteria.where("saleItems").elemMatch(Criteria.where("itemCode").regex(saleDynamicQuery.getItemCode())));
        }
         if(saleDynamicQuery.getItemName()!=null && !saleDynamicQuery.getItemName().equalsIgnoreCase("")){
          criteria.add(Criteria.where("saleItems").elemMatch(Criteria.where("name").regex(saleDynamicQuery.getItemName())));
        }
        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
        }
       // return mongoTemplate.findDistinct(query, "id", Sale.class);
        return mongoTemplate.find(query, Sale.class);
    }
    private Date getEndDate(SaleDynamicQuery saleDynamicQuery){
        if(saleDynamicQuery.getEndDate()!=null){
            return saleDynamicQuery.getEndDate();
        }else{
            if(saleDynamicQuery.getStartDate()!=null){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(saleDynamicQuery.getStartDate());
                calendar.add(Calendar.DATE, +1);
                Date endDate = calendar.getTime();
                return endDate;
            }
        }
        return null;
    }

   
}
