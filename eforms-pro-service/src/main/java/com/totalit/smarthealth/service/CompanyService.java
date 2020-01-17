/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.service;

import com.totalit.smarthealth.domain.Company;

/**
 *
 * @author roy
 */
public interface CompanyService extends GenericService<Company> {
    Long countByActive(Boolean active);
}
