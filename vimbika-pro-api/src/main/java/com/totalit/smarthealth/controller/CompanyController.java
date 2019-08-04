/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.controller;

import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.service.CompanyService;
import com.totalit.smarthealth.service.UserService;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author roy
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/company")
public class CompanyController {
    
    public static final Logger logger = LoggerFactory.getLogger(CompanyController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService service;
    
    @PostMapping("/save")
    @ApiOperation("Persists Company to Collection")
    public ResponseEntity<Map<String, Object>> save(@RequestBody Company company) {
        Map<String, Object> response = new HashMap<>();
        boolean exist = false;
        try{
        if (!service.checkDuplicate(company, company)) {
            Company c = service.save(company);
            response.put("item", c);
        } else {
            exist = true;
        }
        }
        catch(Exception ex){
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(exist){
            response.put("duplicate", true);
            return new ResponseEntity<>(response, HttpStatus.OK); 
        } 
        response.put("message", "Company Saved Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/get-all")
    @ApiOperation("Returns All Companies")
    public ResponseEntity<?> getAllCompanies() {
        logger.info("Retrieving All Companies{}");
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }
}
