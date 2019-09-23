/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.controller;

import com.totalit.smarthealth.domain.Account;
import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.InventoryItem;
import com.totalit.smarthealth.domain.PaymentReceived;
import com.totalit.smarthealth.domain.PaymentType;
import com.totalit.smarthealth.domain.Sale;
import com.totalit.smarthealth.domain.util.SaleStatus;
import com.totalit.smarthealth.service.AccountService;
import com.totalit.smarthealth.service.CompanyService;
import com.totalit.smarthealth.service.InventoryItemService;
import com.totalit.smarthealth.service.PaymentReceivedService;
import com.totalit.smarthealth.service.SaleService;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.util.AppUtil;
import com.totalit.smarthealth.util.DateUtil;
import com.totalit.smarthealth.util.EndPointUtil;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author roy
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/sale")
public class SaleController {
    public static final Logger logger = LoggerFactory.getLogger(SaleController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private SaleService saleService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CompanyService companyService;    
    @Autowired
    private InventoryItemService inventoryItemService;
    @Autowired
    private PaymentReceivedService paymentReceivedService;
    
    @PostMapping("/save")
    @ApiOperation("Persists Sale to Collection")
    public ResponseEntity<Map<String, Object>> save(@RequestHeader(value = "Company") String company, @RequestBody Sale sale) {
        Map<String, Object> response = new HashMap<>();
        Company c = EndPointUtil.getCompany(company);
        sale.setCompany(c);
        String payer = sale.getCustomer() == null ? "WALK_IN_CUSTOMER" : sale.getCustomer().getName();   
        sale.setIsWalkInCustomer(sale.getCustomer() == null);
        try{
            if(sale.getSaleStatus().equals(SaleStatus.COMPLETE)){
                Long count = saleService.countByCompany(c);
                int recordNumber = 1 + count.intValue();
                String ref = AppUtil.getReferenceNumber(recordNumber);
                sale.setReferenceNumber(ref);
                sale.getPaymentTypes().forEach(t ->{
                    t.setCompany(c);
                    t.setReference(ref);
                    t.setPayer(payer);
                    t.setDateTime(LocalDateTime.now());
                });
                List<PaymentReceived> paymentsReceived = paymentReceivedService.saveAll(sale.getPaymentTypes());                
                sale.setPaymentTypes(paymentsReceived);
            }else{               
                if(sale.getReferenceNumber()==null){
                sale.setReferenceNumber("HOLD_REF-" + AppUtil.randomNumber());
                }
            }
            sale.setTimeIniated(DateUtil.getLocalDateTimeFromString(sale.getTimeInit()));
            Sale s = saleService.save(sale);
            deductStock(sale.getSaleItems());
//            // Credit Company Account 
//            if(sale.getSaleStatus().equals(SaleStatus.COMPLETE)){
//                if(s!=null){
//                    for(PaymentType type: sale.getPaymentTypes()){
//                            Account account = new Account();
//                            account.setAmount(type.getAmount());
//                            account.setPayer(payer);
//                            account.setPaymentType(type);
//                            account.setReferenceNumber(s.getReferenceNumber());
//                            account.setCustomer(sale.getCustomer());
//                            accountService.save(account);
//                          }
//                    deductStock(sale.getSaleItems()); // Deduct Stock for Every Sale Item.
//                }
//            }
            response.put("item", s);
        } 
        catch(Exception ex){
            ex.printStackTrace();
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        response.put("message", "Sale Saved Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/on-hold")
    @ApiOperation("Returns All On Hold Sales")
    public ResponseEntity<?> getOnHoldSale(@RequestHeader(value = "Company") String company) {
        logger.info("Retrieving All On Hold Sales By Company{} and Date");
        Company c = EndPointUtil.getCompany(company);
        return new ResponseEntity<>(saleService.findByCompanyAndDateCreatedAndSaleStatus(c, new Date(), SaleStatus.ON_HOLD), HttpStatus.OK);
    }
    @GetMapping("/count-on-hold")
    @ApiOperation("Count All On Hold Sales")
    public ResponseEntity<?> count(@RequestHeader(value = "Company") String company) {
        logger.info("Counting All On Hold Sales By Company{} and Date");
        Company c = EndPointUtil.getCompany(company);
        return new ResponseEntity<>(saleService.countByCompanyAndDateCreatedAndSaleStatus(c, new Date(), SaleStatus.ON_HOLD), HttpStatus.OK);
    }
    
    @GetMapping("/by-date")
    @ApiOperation("Returns All Sales By Date")
    public ResponseEntity<?> getAll(@RequestHeader(value = "Company") String company, @RequestParam("date") String date) {
        logger.info("Retrieving All Sales By Date & Company{}");
        Company c = EndPointUtil.getCompany(company);
        Date d = DateUtil.getDateFromStringApi(date);
        return new ResponseEntity<>(saleService.findSaleByDateCreated(d, c), HttpStatus.OK);
    }
    
    public void deductStock(Set<InventoryItem> items){
        for(InventoryItem item: items){
            InventoryItem inventoryItem = inventoryItemService.get(item.getId());
            inventoryItem.setAvailableItems(inventoryItem.getAvailableItems() - item.getQuantity());
            inventoryItemService.save(inventoryItem);
        }
    }
}
