/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.totalit.smarthealth.domain.*;
import com.totalit.smarthealth.domain.util.SaleStatus;
import com.totalit.smarthealth.service.*;
import com.totalit.smarthealth.util.AppUtil;
import com.totalit.smarthealth.util.DateUtil;
import com.totalit.smarthealth.util.EndPointUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 *
 * @author roy
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/create-form")
public class CreateQuestionnaireController {
    public static final Logger logger = LoggerFactory.getLogger(CreateQuestionnaireController.class);
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
    @Autowired
    private BranchStockService branchStockService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private CreateFormService createFormService;
    @Autowired
    private AnswerFormService answerFormService;

    @PostMapping("/create")
    @ApiOperation("Persists Sale to Collection")
    public ResponseEntity<Map<String, Object>> createForm(@RequestBody CreateForm createForm) {
        Map<String, Object> response = new HashMap<>();
        System.err.println(createForm);
//        DBObject dbObject = (DBObject) JSON.parse(string);
//        Mongo mongo = new Mongo("localhost", 27017);
//        DB db = mongo.getDB("vacancyafrica");
//        DBCollection coll = db.getCollection("createform");
//        coll.save(dbObject);
        CreateForm form = createFormService.save(createForm);


        response.put("item", form);
        response.put("message", "CreataeForm Saved Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/answers")
    @ApiOperation("saves answers by user")
    public ResponseEntity<Map<String, Object>> saveAnswers(@RequestBody AnswerForm answerForm) {
        Map<String, Object> response = new HashMap<>();
        System.err.println(answerForm);
        AnswerForm form = answerFormService.save(answerForm);


        response.put("item", form);
        response.put("message", "CreataeForm Saved Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/get/{item}")
    @ApiOperation(value = "Returns User of Id passed as parameter", response = CreateForm.class)
    public ResponseEntity<CreateForm> getItem(@ApiParam(name = "item", value = "Id used to fetch the object") @PathVariable("item") String item) {
        return new ResponseEntity<>(createFormService.findByFormName(item), HttpStatus.OK);
    }

    @GetMapping("/get-all")
    @ApiOperation(value = "returns all forms as list", response = CreateForm.class)
    public ResponseEntity<?> getAll() throws JsonProcessingException {
        logger.info("Retrieving All Forms{}");
        List<CreateForm> forms = createFormService.getAll();
//                ObjectMapper objectMapper = new ObjectMapper();
//        System.err.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(forms));

        return new ResponseEntity<>(forms, HttpStatus.OK);
    }
    
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
                    t.setCurrency(sale.getCurrency());
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
            amountSpent(sale);
            deductStock(sale.getSaleItems(), s.getBranch());
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
    
    public void deductStock(Set<InventoryItem> items, Branch branch){
        if(branch.getName().equalsIgnoreCase("MAIN_BRANCH")){
            for(InventoryItem item: items){
                InventoryItem inventoryItem = inventoryItemService.get(item.getId());
                inventoryItem.setAvailableItems(inventoryItem.getAvailableItems() - item.getQuantity());
                inventoryItem.setSaleCount(inventoryItem.getSaleCount() + item.getQuantity());
                inventoryItemService.save(inventoryItem);
            }
        }else{
           for(InventoryItem item: items){
                BranchStock branchStock = branchStockService.getBranchInventoryItem(branch, item, Boolean.TRUE);
                InventoryItem inventoryItem = inventoryItemService.get(item.getId());
                inventoryItem.setSaleCount(inventoryItem.getSaleCount() + item.getQuantity());
                InventoryItem i = inventoryItemService.save(inventoryItem);
                branchStock.setStock(branchStock.getStock() - item.getQuantity());
                branchStock.setSaleCount(branchStock.getSaleCount() + item.getQuantity());
                branchStock.setItem(i);
                branchStockService.save(branchStock);
                
           } 
        }
    }
    private void amountSpent(Sale sale){
        if(sale.getCustomer()!=null){
            Customer customer = customerService.get(sale.getCustomer().getId());
            double amt = sale.getAmountAfterDiscount()/sale.getCurrency().getRate();
            customer.setAmountSpent(customer.getAmountSpent() + amt);
            customerService.save(customer);
        }
    }
}
