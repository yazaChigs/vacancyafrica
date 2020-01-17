/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.repository;

import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.Sale;
import com.totalit.smarthealth.domain.util.SaleStatus;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author roy
 */
@Repository
public interface SaleRepository extends AbstractCompanyRepository<Sale, String>{
     Long countByCompany(Company company);
     List<Sale> findByCompanyAndSaleStatus(Company company, SaleStatus saleStatus);
     Long countByCompanyAndSaleStatus(Company company, SaleStatus saleStatus);
    
}
