/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.controller;


import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.User;
import com.totalit.smarthealth.domain.UserRole;
import com.totalit.smarthealth.domain.Visitor;
import com.totalit.smarthealth.service.UserRoleService;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.service.VisitorService;
import com.totalit.smarthealth.util.EndPointUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kanaz
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/visitor")
public class VisitorResource {

    public static final Logger logger = LoggerFactory.getLogger(VisitorResource.class);
    @Autowired
    private VisitorService service;
    @Autowired
    private UserRoleService userRoleService;


    @PostMapping("/authentication")
    @ApiOperation(value = "authanticates visitor by passed as parameter")
    public ResponseEntity<?> getVisitorAuthantication(@RequestParam Map<String,String> credentials) {
        Map<String, Object> response = new HashMap<>();
        String userName=credentials.get("userName");
        String password=credentials.get("password");
        Visitor v = service.findByUserName(userName);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (passwordEncoder.matches(password, v.getPassword())) {
            response.put("message",v);
        }else {
            response.put("message","visitor doesn't exist visitor");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(v, HttpStatus.OK);
    }

    @PostMapping("update")
    @ApiOperation("Persists User object to the database")
    public ResponseEntity<?> updateUser(@RequestBody Visitor visitor) {
        Map<String, Object> response = new HashMap<>();

        return new ResponseEntity<>(service.save(visitor), HttpStatus.OK);
    }

    @PostMapping("/save")
    @ApiOperation("Persists User object to the database")
    public ResponseEntity<Map<String, Object>> saveUser( @RequestBody Visitor visitor) {
        Map<String, Object> response = new HashMap<>();
//        UserRole r = userRoleService.getByName("ROLE_VISITOR");
        visitor.setUserType("VISITOR");
        boolean exist = false;
        try {
              if(!service.checkDuplicate(visitor, visitor)){
                Visitor u =  service.save(visitor);
                response.put("item", u);
              }else{
                    exist = true;
              }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            response.put("message", "System error occurred saving item");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(exist){
            response.put("duplicate", true);
            return new ResponseEntity<>(response, HttpStatus.OK); 
        }
        response.put("message", "User Saved Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
     @GetMapping("/get-all")
    @ApiOperation("Returns All Users")
    public ResponseEntity<?> getAll() {
        logger.info("Retrieving All Users By Company{}");
//        Company c = EndPointUtil.getCompany(company);
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }
     @GetMapping("/get-item/{id}")
    @ApiOperation(value = "Returns User of Id passed as parameter", response = Visitor.class)
    public ResponseEntity<Visitor> getItem(@ApiParam(name = "id", value = "Id used to fetch the object") @PathVariable("id") String id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }



    @PostMapping("/change-password")
    @ApiOperation("Change User Password")
    public ResponseEntity<Map<String, Object>> changePassword(@RequestParam("id") String id, @RequestParam("password") String password) {
        logger.info("Changing User Password{}");
        HashMap<String, Object> response = new HashMap<>();
        try{
        Visitor visitor = service.get(id);
            visitor.setPassword(password);
        Visitor u = service.changePassword(visitor);
        response.put("message", "Password Changed Successfully");
        response.put("user", u);
        }
        catch(Exception e){
            e.printStackTrace();
          response.put("message", e.getMessage());  
           return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    
    @GetMapping("/inactive")
    @ApiOperation("Returns All Inactive User Accounts")
    public ResponseEntity<?> getInactiveAccounts() {
        logger.info("Retrieving Inactive User Accounts{}");
        return new ResponseEntity<>(service.getAllInActive(), HttpStatus.OK);
    }
    @GetMapping("/activate/{userId}")
    @ApiOperation("Activate User Account")
    public ResponseEntity<Void> activateUserAccount(@PathVariable("userId") String id) {
        logger.info("Activate User Account{}");   
        Visitor visitor = service.get(id);
        visitor.setActive(Boolean.TRUE);
        service.save(visitor);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @DeleteMapping("/delete")
//    @ApiOperation("Deactivate User Account")
//    public ResponseEntity<Map<String, Object>> deactivateUser(@ApiParam(name = "id", value = "Id of object to be deleted") @RequestParam("id") String id) {
//        logger.info("Deactivating User Account");
//        Map<String, Object> response = new HashMap<>();
//        try {
//            service.delete(service.get(id));
//            
//        } catch (Exception ex) {
//            response.put("message", "System error occurred deleting item");
//            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        response.put("message", "User Account Deactivated");
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("Set Inactive to User Object")
    public ResponseEntity<Map<String, Object>> delete(@ApiParam(name = "id", value = "id for object to be deleted") @PathVariable("id") String id) {
        logger.info("Set Inactive on User Object");
        Map<String, Object> response = new HashMap<>();
        String itemMessage = "";
        try {
          Visitor visitor = service.get(id);
            visitor.setActive(Boolean.FALSE);
            visitor.setDeleted(Boolean.TRUE);
          service.save(visitor);
           
        } catch (Exception ex) {
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        itemMessage = itemMessage + " Deleted Successfully"; 
        response.put("message", itemMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private String encryptPassword(String pass){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(pass);
        return hashedPassword;
    }
    
}
