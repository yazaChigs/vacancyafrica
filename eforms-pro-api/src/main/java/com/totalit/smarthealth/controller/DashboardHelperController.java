/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.controller;

import com.totalit.smarthealth.service.AccountService;
import com.totalit.smarthealth.service.SaleService;
import com.totalit.smarthealth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author roy
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/dashboard-helper")
public class DashboardHelperController {
     public static final Logger logger = LoggerFactory.getLogger(DashboardHelperController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private SaleService saleService;
    @Autowired
    private AccountService accountService;
}
