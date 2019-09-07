/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.service;

import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.Sale;
import com.totalit.smarthealth.domain.util.SaleStatus;
import java.util.Date;
import java.util.List;

/**
 *
 * @author roy
 */
public interface SaleService extends GenericCompanyService<Sale>{
    Long countByCompany(Company company);
    List<Sale> findByCompanyAndDateCreatedAndSaleStatus(Company company, Date date, SaleStatus saleStatus);
    Long countByCompanyAndDateCreatedAndSaleStatus(Company company, Date date, SaleStatus saleStatus);   
    List<Sale> findSaleByDateCreated(Date date, Company company);
}
