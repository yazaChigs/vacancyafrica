/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.controller;

import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.Currency;
import com.totalit.smarthealth.domain.Sale;
import com.totalit.smarthealth.query.CurrencyAmount;
import com.totalit.smarthealth.query.SaleDynamicQuery;
import com.totalit.smarthealth.service.AccountService;
import com.totalit.smarthealth.service.CompanyService;
import com.totalit.smarthealth.service.CurrencyService;
import com.totalit.smarthealth.service.InventoryItemService;
import com.totalit.smarthealth.service.PaymentReceivedService;
import com.totalit.smarthealth.service.SaleService;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.util.AppUtil;
import com.totalit.smarthealth.util.DateUtil;
import com.totalit.smarthealth.util.EndPointUtil;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import static java.util.stream.Collectors.groupingBy;
import java.util.Date;
import java.util.List;
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
@RequestMapping("/api/reports/sales")
public class SaleReportController {
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
    private CurrencyService currencyService;    
    @Autowired
    private InventoryItemService inventoryItemService;
    @Autowired
    private PaymentReceivedService paymentReceivedService;
    
    @PostMapping("/dynamic-query")
    @ApiOperation("Persists Sale to Collection")
    public ResponseEntity<?> save(@RequestHeader(value = "Company") String company, @RequestBody SaleDynamicQuery saleDynamicQuery ) {
        logger.info("Retrieving All Sales By SaleDynamicQuery{}"); 
        Company c = EndPointUtil.getCompany(company);
        saleDynamicQuery.setCompany(c);
        try{
            if(saleDynamicQuery.getEndDateString()!=null){
                saleDynamicQuery.setEndDate(DateUtil.getDateFromStringApi(saleDynamicQuery.getEndDateString()));
            }
            if(saleDynamicQuery.getStartDateString()!=null){
                saleDynamicQuery.setStartDate(DateUtil.getDateFromStringApi(saleDynamicQuery.getStartDateString()));
            }
             List<Sale> sales = saleService.findBySearchSaleDto(saleDynamicQuery);
                     return new ResponseEntity<>(sales, HttpStatus.OK);
         }catch(Exception exception){
             exception.printStackTrace();
             return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }
    @GetMapping("/currency-amount")
    @ApiOperation("Returns All Sales By Date")
    public ResponseEntity<?> getAll(@RequestHeader(value = "Company") String company, @RequestParam("date") String date) {
        logger.info("Retrieving All Sales By Date & Company{}");
        Company c = EndPointUtil.getCompany(company);
        try{
            System.err.println("===================");
             System.err.println(date);
              System.err.println("===================");
        Date d = DateUtil.getDateFromStringApi(date);
        List<Sale> sales = saleService.findSaleByDateCreated(d, c);
        List<CurrencyAmount> currencyAmount = new ArrayList<>();
        List<Currency> currency = currencyService.getAll(c);
        Currency base = currencyService.getBaseCurrency(c);
        Map<String, List<Sale>> s = sales.stream().collect(groupingBy(sa -> sa.getCurrency()!=null ? sa.getCurrency().getId() : base.getId())); // Group Sales By Currency
        for(Currency cur : currency){
             s.forEach( (t, u) -> { // Loop grouped Sales 
                 if(t.equalsIgnoreCase(cur.getId())){ //check if key of currency equal to currency list
                     double sum = u.stream().mapToDouble(Sale::getAmountAfterDiscount).sum(); //sum all sales according to currency
                     CurrencyAmount ca  = new CurrencyAmount();
                     ca.setAmount(AppUtil.roundNumber(sum));
                     ca.setCurrency(cur);
                     currencyAmount.add(ca);
                 }
             });
        }              
        return new ResponseEntity<>(currencyAmount, HttpStatus.OK);
        }
        catch(Exception exception){
             exception.printStackTrace();
             return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }
}
