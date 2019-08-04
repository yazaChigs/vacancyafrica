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
public interface GenericCompanyService <T extends Serializable> extends GenericService<T> {
    public List<T> getByCompany(Company company);
}
