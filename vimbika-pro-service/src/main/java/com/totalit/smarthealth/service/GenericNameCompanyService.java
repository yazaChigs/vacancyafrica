/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.service;

import com.totalit.smarthealth.domain.Company;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author roy
 * @param <T>
 */
public interface GenericNameCompanyService <T extends Serializable> extends GenericService<T>{
    public T getByNameAndCompany(String name, Company company);
    public Boolean checkDuplicate(T current, T old, Company company);
    public List<T> getAll(Company company);
    
}
