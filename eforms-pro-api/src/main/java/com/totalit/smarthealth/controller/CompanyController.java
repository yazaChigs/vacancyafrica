/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.controller;

import static com.totalit.smarthealth.controller.CompanyController.logger;
import com.totalit.smarthealth.domain.Branch;
import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.Currency;
import com.totalit.smarthealth.domain.ExpenseCategory;
import com.totalit.smarthealth.service.BranchService;
import com.totalit.smarthealth.service.CompanyService;
import com.totalit.smarthealth.service.CurrencyService;
import com.totalit.smarthealth.service.ExpenseCategoryService;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.service.impl.StorageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private ExpenseCategoryService expenseCategoryService;
    @Autowired
    private StorageService storageService;
     @Autowired
    private BranchService branchService;
     @Autowired
    private CurrencyService currencyService;
    
    @PostMapping("/save")
    @ApiOperation("Persists Company to Collection")
    public ResponseEntity<Map<String, Object>> save(@RequestBody Company company) {
        Map<String, Object> response = new HashMap<>();
        boolean exist = false;
        String id = company.getId();
        try{
        if (!service.checkDuplicate(company, company)) {
            Company c = service.save(company);
            if(id == null){
                saveStaticData(c);
            }
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
     @GetMapping("/get-item/{id}")
    @ApiOperation(value = "Returns Company of Id passed as parameter", response = Company.class)
    public ResponseEntity<Company> getItem(@ApiParam(name = "id", value = "Id used to fetch the object") @PathVariable("id") String id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{id}")
    @ApiOperation("Set Inactive to Company Object")
    public ResponseEntity<Map<String, Object>> delete(@ApiParam(name = "id", value = "id for object to be deleted") @PathVariable("id") String id) {
        logger.info("Set Inactive on Company Object");
        Map<String, Object> response = new HashMap<>();
        String itemMessage = "";
        try {
          Company company = service.get(id);
          company.setActive(Boolean.FALSE);
          company.setDeleted(Boolean.TRUE);
          service.save(company);
           
        } catch (Exception ex) {
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        itemMessage = itemMessage + " Deleted Successfully"; 
        response.put("message", itemMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/count")
    @ApiOperation("Count to Companies")
    public ResponseEntity<Map<String, Object>> countCompanies() {
        logger.info("Count Companies");
        Map<String, Object> response = new HashMap<>();        
        try {
            long inactive = service.countByActive(Boolean.FALSE);
            response.put("inactive", inactive);
            long active = service.countByActive(Boolean.TRUE);
            response.put("active", active);
        } catch (Exception ex) {
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/image")
    @ApiOperation("Save Company Logo")
    public ResponseEntity<Map<String, Object>> create(@RequestParam("image") MultipartFile file, @RequestParam("id") String id) {
        logger.info("Saving company logo");
        Map<String, Object> response = new HashMap<>();
 try {
        Company profile = service.get(id);
              
        String[] fileFrags = file.getOriginalFilename().split("\\.");
        String extension = fileFrags[fileFrags.length - 1];
        String fileName = profile.getName().concat(".").concat(extension).toLowerCase();
        String dir = "VIMBIKA-LOGOS";
        Path path = storageService.createNewDirectory(dir);
        profile.setLogo(dir.concat(String.valueOf(File.separatorChar)).concat(fileName));
       
            storageService.storeFile(file, path, fileName);
            service.save(profile);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.put("message", "System error occurred saving item " + ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message", "Logo Saved Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/logo/{companyId}")
    @ResponseBody
    public ResponseEntity<org.springframework.core.io.Resource> getFile(@PathVariable("companyId") String companyId) {
          if(companyId != null) {
              Company company = service.get(companyId);
              String name = company.getLogo();
              org.springframework.core.io.Resource file = storageService.loadFile(name);
              if(file != null){
              return ResponseEntity.ok()
                      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                      .body(file);
              }
          }
        return null;
    }
    public void saveStaticData(Company company){
        
        // Save Purchase Expense
        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setCompany(company);
        expenseCategory.setName("PURCHASE EXPENSE");
        expenseCategory.setDescription("Expense Category for all purchases.");
        expenseCategoryService.save(expenseCategory);
        Branch branch = new Branch();
        branch.setName("MAIN_BRANCH");
        branch.setActive(Boolean.TRUE);
        branch.setIsDefault(Boolean.TRUE);
        branch.setCity(company.getCity());
        branch.setStateProvince(company.getStateProvince());
        branch.setContactNumber(company.getOfficePhone());
        branch.setCompany(company);
        branch.setStreet(company.getStreet());
        branchService.save(branch);
        Currency currency = new Currency();
        currency.setActive(Boolean.TRUE);
        currency.setCompany(company);
        currency.setIsBaseCurrency(Boolean.TRUE);
        currency.setSymbol("$");
        currency.setRate(1.0f);
        currency.setName("USD");
        currencyService.save(currency);
    }
}
