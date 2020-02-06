/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.repository;

import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.Customer;
import com.totalit.smarthealth.domain.Visitor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author roy
 */
@Repository
public interface VisitorRepository extends AbstractRepo<Visitor, String>{
    public Visitor findByActiveAndUserName(Boolean active, String userName);
    public Boolean existsByActiveAndUserNameIgnoreCase(Boolean active, String userName);
    public Visitor findByUserNameAndPasswordAndActive(String userName, String password, Boolean active);
}
