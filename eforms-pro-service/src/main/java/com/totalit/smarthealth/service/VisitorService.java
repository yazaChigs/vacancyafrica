/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.service;

import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.Customer;
import com.totalit.smarthealth.domain.Visitor;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author roy
 */
public interface VisitorService extends GenericService<Visitor>{
    public Visitor findByUserName(String name);

    public String getCurrentUsername();

    public Visitor getCurrentUser();
    List<Visitor> getAllInActive();
    /*
     change pass
    */
    public Visitor changePassword(Visitor t);
    public Visitor findByUserNameAndPasswordAndActive(String userName, String password, Boolean active);
    public Optional<Visitor> findUserByResetToken(String resetToken);
}
