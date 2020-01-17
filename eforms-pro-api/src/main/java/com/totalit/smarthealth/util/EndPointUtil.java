/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.util;

import com.totalit.smarthealth.domain.Company;

/**
 *
 * @author roy
 */
public class EndPointUtil {
    public static Company getCompany(String id){
        Company company = new Company();
        company.setId(id);
        return company;
    }
}
