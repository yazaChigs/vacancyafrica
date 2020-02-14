package com.totalit.smarthealth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.totalit.smarthealth.domain.ApplicationForm;
import com.totalit.smarthealth.domain.Branch;
import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.service.*;
import com.totalit.smarthealth.util.EndPointUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/application")
public class ApplicantController {

    public static final Logger logger = LoggerFactory.getLogger(BranchController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationFormService service;
    @Autowired
    private CreateFormService formService;
    @Autowired
    private CompanyService companyService;

    @PostMapping("/save")
    @ApiOperation("Persists Branch to Collection")
    public ResponseEntity<Map<String, Object>> save( @RequestBody ApplicationForm applicationForm) {
        Map<String, Object> response = new HashMap<>();
        Company c = new Company();
        if(!applicationForm.getCompanyName().equals(null)) {
            c = companyService.findByNameAndActive(applicationForm.getCompanyName(), Boolean.TRUE);
        }else {
            companyService.findByNameAndActive(
                    formService.findByFormName(
                            applicationForm.getFormName()).getCompanyName(),Boolean.TRUE);
        }
        applicationForm.setCompany(c);
                ApplicationForm s = service.save(applicationForm);
        response.put("message", "Branch Saved Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-all")
    @ApiOperation("Returns All Branches")
    public ResponseEntity<?> getAll(@RequestHeader(value = "Company") String company) {
        logger.info("Retrieving All applications By Company{}");
        Company c = EndPointUtil.getCompany(company);
        return new ResponseEntity<>(service.getByCompany(c), HttpStatus.OK);
    }

    @GetMapping("/get-item/{id}")
    @ApiOperation(value = "Returns application of Id passed as parameter", response = ApplicationForm.class)
    public ResponseEntity<ApplicationForm> getItem(@ApiParam(name = "id", value = "Id used to fetch the object") @PathVariable("id") String id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("Set Inactive to application Object")
    public ResponseEntity<Map<String, Object>> delete(@ApiParam(name = "id", value = "id for object to be deleted") @PathVariable("id") String id) {
        logger.info("Set Inactive on application Object");
        Map<String, Object> response = new HashMap<>();
        String itemMessage = "";
        try {
            ApplicationForm applicationForm = service.get(id);
            applicationForm.setActive(Boolean.FALSE);
            applicationForm.setDeleted(Boolean.TRUE);
            service.save(applicationForm);

        } catch (Exception ex) {
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        itemMessage = itemMessage + " Deleted Successfully";
        response.put("message", itemMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
