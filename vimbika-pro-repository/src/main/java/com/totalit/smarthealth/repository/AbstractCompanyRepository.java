/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.repository;

import com.totalit.smarthealth.domain.Company;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * @author roy
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface AbstractCompanyRepository <T , ID extends Serializable> extends AbstractRepo<T, ID>{
    public List<T> findByActiveAndCompany(Boolean active, Company company); 
}
