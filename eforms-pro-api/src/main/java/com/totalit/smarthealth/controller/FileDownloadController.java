/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.controller;

import com.totalit.smarthealth.domain.Sale;
import com.totalit.smarthealth.service.AccountService;
import com.totalit.smarthealth.service.BranchStockService;
import com.totalit.smarthealth.service.CompanyService;
import com.totalit.smarthealth.service.CurrencyService;
import com.totalit.smarthealth.service.CustomerService;
import com.totalit.smarthealth.service.InventoryItemService;
import com.totalit.smarthealth.service.PaymentReceivedService;
import com.totalit.smarthealth.service.SaleService;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.service.impl.StorageService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author roy
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/file/download")
public class FileDownloadController {
    public static final Logger logger = LoggerFactory.getLogger(FileDownloadController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private SaleService saleService;
   
    @Autowired
    private StorageService storageService;

    public FileDownloadController() {
    }
    
    
     @RequestMapping(value = "/invoice", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getIn(@RequestParam("saleId") String saleId) throws IOException {
      
        Sale sale = saleService.get(saleId);
        ByteArrayInputStream bis = null;
        
        Resource file = storageService.loadFile(sale.getCompany().getLogo());
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    
    
    
}
