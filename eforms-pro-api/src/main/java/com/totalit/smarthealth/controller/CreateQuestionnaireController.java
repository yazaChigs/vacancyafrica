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
    private CreateFormService service;
    @Autowired
    private ApplicationFormService answerFormService;

    @PostMapping("/create")
    @ApiOperation("Persists Sale to Collection")
    public ResponseEntity<Map<String, Object>> createForm(@RequestHeader(value = "Company") String company,@RequestBody CreateForm createForm) {
//        Map<String, Object> response = new HashMap<>();
//        System.err.println(createForm);
//        CreateForm form = createFormService.save(createForm);
//
//
//        response.put("item", form);
//        response.put("message", "CreataeForm Saved Successfully");
//        return new ResponseEntity<>(response, HttpStatus.OK);
        Map<String, Object> response = new HashMap<>();
        Company c = EndPointUtil.getCompany(company);
        createForm.setCompany(c);
        CreateForm form = service.save(createForm);
//        boolean exist = false;
//        try{
//            if (!service.checkDuplicate(createForm, createForm, c)) {
//                CreateForm s = service.save(createForm);
//                response.put("item", s);
//            } else {
//                exist = true;
//            }
//        }
//        catch(Exception ex){
//            response.put("message", ex.getMessage());
//            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        if(exist){
//            response.put("duplicate", true);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        }
        response.put("item", form);
        response.put("message", "Branch Saved Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/answers")
    @ApiOperation("saves answers by user")
    public ResponseEntity<Map<String, Object>> saveAnswers(@RequestBody ApplicationForm answerForm) {
        Map<String, Object> response = new HashMap<>();
        System.err.println(answerForm);
        ApplicationForm form = answerFormService.save(answerForm);


        response.put("item", form);
        response.put("message", "CreataeForm Saved Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/get/{item}")
    @ApiOperation(value = "Returns User of Id passed as parameter", response = CreateForm.class)
//    public ResponseEntity<CreateForm> getItem(@RequestHeader(value = "Company") String company, @ApiParam(name = "item", value = "Id used to fetch the object") @PathVariable("item") String item) {
        public ResponseEntity<CreateForm> getItem( @ApiParam(name = "item", value = "Id used to fetch the object") @PathVariable("item") String item) {
//        Company c =
        Map<String, Object> response = new HashMap<>();
        System.err.println(item);
        CreateForm f = service.findByFormName(item);
        if(f== null) {
            response.put("message","form not found");
//            return new ResponseEntity<String, Object>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(f, HttpStatus.OK);
    }

    @GetMapping("/get-all")
    @ApiOperation(value = "returns all forms as list", response = CreateForm.class)
    public ResponseEntity<?> getAll(@RequestHeader(value = "Company") String company) throws JsonProcessingException {
        logger.info("Retrieving All Forms{}");
        Company c = EndPointUtil.getCompany(company);
        List<CreateForm> forms = service.getByCompany(c);
//                ObjectMapper objectMapper = new ObjectMapper();
//        System.err.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(forms));

        return new ResponseEntity<>(forms, HttpStatus.OK);
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
    

}
