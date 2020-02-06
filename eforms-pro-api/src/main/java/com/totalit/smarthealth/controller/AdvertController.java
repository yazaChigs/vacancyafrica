package com.totalit.smarthealth.controller;

import com.totalit.smarthealth.domain.Advert;
import com.totalit.smarthealth.domain.ApplicationForm;
import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.service.AdvertService;
import com.totalit.smarthealth.service.ApplicationFormService;
import com.totalit.smarthealth.service.CompanyService;
import com.totalit.smarthealth.service.UserService;
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
@RequestMapping("/api/advert")
public class AdvertController {

    public static final Logger logger = LoggerFactory.getLogger(BranchController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private AdvertService service;
    @Autowired
    private CompanyService companyService;

    @PostMapping("/save")
    @ApiOperation("Persists Advert to Collection")
    public ResponseEntity<Map<String, Object>> save(@RequestHeader(value = "Company") String company, @RequestBody Advert advert) {
        Map<String, Object> response = new HashMap<>();
        Company c = EndPointUtil.getCompany(company);
        advert.setCompany(c);
//                        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            System.err.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(applicationForm));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

//        boolean exist = false;
//        try{
//            if (!service.checkDuplicate(applicationForm, applicationForm, c)) {
                Advert s = service.save(advert);
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
        response.put("message", "Ad Saved Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-all")
    @ApiOperation("Returns All Ads")
//    public ResponseEntity<?> getAll(@RequestHeader(value = "Company") String company) {
        public ResponseEntity<?> getAll() {
        logger.info("Retrieving All ads ");
//        Company c = EndPointUtil.getCompany(company);
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @GetMapping("/get-item/{id}")
    @ApiOperation(value = "Returns application of Id passed as parameter", response = ApplicationForm.class)
    public ResponseEntity<Advert> getItem(@ApiParam(name = "id", value = "Id used to fetch the object") @PathVariable("id") String id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("Set Inactive to application Object")
    public ResponseEntity<Map<String, Object>> delete(@ApiParam(name = "id", value = "id for object to be deleted") @PathVariable("id") String id) {
        logger.info("Set Inactive on application Object");
        Map<String, Object> response = new HashMap<>();
        String itemMessage = "";
        try {
            Advert advert = service.get(id);
            advert.setActive(Boolean.FALSE);
            advert.setDeleted(Boolean.TRUE);
            service.save(advert);

        } catch (Exception ex) {
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        itemMessage = itemMessage + " Deleted Successfully";
        response.put("message", itemMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
