/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.controller;

import static com.totalit.smarthealth.controller.PurchaseController.logger;
import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.service.CompanyService;
import com.totalit.smarthealth.service.CurrencyService;
import com.totalit.smarthealth.service.PaymentReceivedService;
import com.totalit.smarthealth.service.PaymentService;
import com.totalit.smarthealth.util.EndPointUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author roy
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/payments/received")
public class PaymentsReceivedController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CurrencyService currencyService;
    
    @Autowired
    private PaymentReceivedService paymentService;
       
    @GetMapping("/get-all")
    @ApiOperation("Returns All Payments Received")
    public ResponseEntity<?> getAll(@RequestHeader(value = "Company") String company) {
        logger.info("Retrieving All Payments By Company{}");
        Company c = EndPointUtil.getCompany(company);
        return new ResponseEntity<>(paymentService.getByCompany(c), HttpStatus.OK);
    }
}
