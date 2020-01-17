/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.controller;

import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.Currency;
import com.totalit.smarthealth.service.CompanyService;
import com.totalit.smarthealth.service.CurrencyService;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.util.EndPointUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author roy
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/currency")
public class CurrencyController {
     public static final Logger logger = LoggerFactory.getLogger(CurrencyController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private CurrencyService service;
    @Autowired
    private CompanyService companyService;
    
    @PostMapping("/save")
    @ApiOperation("Persists Currency to Collection")
    public ResponseEntity<Map<String, Object>> save(@RequestHeader(value = "Company") String company, @RequestBody Currency currency) {
        Map<String, Object> response = new HashMap<>();
        Company c = EndPointUtil.getCompany(company);
        currency.setCompany(c);
        boolean exist = false;
        try{
        if (!service.checkDuplicate(currency, currency, c)) {
            Currency s = service.save(currency);
            response.put("item", s);
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
        response.put("message", "Currency Saved Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/get-all")
    @ApiOperation("Returns All Currencyes")
    public ResponseEntity<?> getAll(@RequestHeader(value = "Company") String company) {
        logger.info("Retrieving All Currencys By Company{}");
        Company c = EndPointUtil.getCompany(company);
        return new ResponseEntity<>(service.getAll(c), HttpStatus.OK);
    }
     @GetMapping("/get-item/{id}")
    @ApiOperation(value = "Returns Currency of Id passed as parameter", response = Currency.class)
    public ResponseEntity<Currency> getItem(@ApiParam(name = "id", value = "Id used to fetch the object") @PathVariable("id") String id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }
    @GetMapping("/active")
    @ApiOperation(value = "Returns Currency Active Currency", response = Currency.class)
    public ResponseEntity<Currency> getActiveCuurency( @RequestHeader(value = "Company") String company) {
          Company c = EndPointUtil.getCompany(company);
        return new ResponseEntity<>(service.getBaseCurrency(c), HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{id}")
    @ApiOperation("Set Inactive to Currency Object")
    public ResponseEntity<Map<String, Object>> delete(@ApiParam(name = "id", value = "id for object to be deleted") @PathVariable("id") String id) {
        logger.info("Set Inactive on Currency Object");
        Map<String, Object> response = new HashMap<>();
        String itemMessage = "";
        try {
          Currency currency = service.get(id);
          currency.setActive(Boolean.FALSE);
          currency.setDeleted(Boolean.TRUE);
          service.save(currency);
           
        } catch (Exception ex) {
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        itemMessage = itemMessage + " Deleted Successfully"; 
        response.put("message", itemMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/base")
    @ApiOperation("Update Base Currency")
    public ResponseEntity<Map<String, Object>> updateBaseCurrency(@RequestBody Currency currency, @RequestHeader(value = "Company") String company) {
        Company c = EndPointUtil.getCompany(company);
        Map<String, Object> response = new HashMap<>();
        try {
            currency.setCompany(c);
            Currency cur = service.save(currency);
            List<Currency> currencies = updateOtherAccounts(cur, c);
            response.put("currencies", currencies);
            if (cur.getIsBaseCurrency()) {
                String message = cur.getName() + " " + "is now Base Currency.";
                response.put("message", message);
            } else {
                response.put("message", "Base Currency Is Not Set.");
            }
        
        } catch (Exception ex) {
            ex.printStackTrace();
            response.put("message", "System error occurred saving item");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    public List<Currency> updateOtherAccounts(Currency currency, Company company) {
        List<Currency> currencys = service.getAll(company);
        List<Currency> settings = new ArrayList<>();
        settings.add(currency);
        for (Currency c : currencys) {
            if (!currency.getId().equalsIgnoreCase(c.getId())) {
                c.setIsBaseCurrency(Boolean.FALSE);
                Currency setting = service.save(c);
                settings.add(setting);
            }
        }
        return settings;
    }

    
}
