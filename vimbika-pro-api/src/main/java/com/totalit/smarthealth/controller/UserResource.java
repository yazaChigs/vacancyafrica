/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.controller;


import com.totalit.smarthealth.domain.User;
import com.totalit.smarthealth.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author kanaz
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/user")
public class UserResource  {

    public static final Logger logger = LoggerFactory.getLogger(UserResource.class);
    @Autowired
    private UserService userService;
   

    @PostMapping("/save")
    @ApiOperation("Persists User object to the database")
    public ResponseEntity<Map<String, Object>> saveUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        boolean exist = false;
        try {
              if(!userService.checkDuplicate(user, user)){
                User u =  userService.save(user); 
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

    @PostMapping("/change-password")
    @ApiOperation("Change User Password")
    public ResponseEntity<Map<String, Object>> changePassword(@RequestParam("id") String id, @RequestParam("password") String password) {
        logger.info("Changing User Password{}");
        HashMap<String, Object> response = new HashMap<>();
        try{
        User user = userService.get(id);
        user.setPassword(password);
        User u = userService.changePassword(user);
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
        return new ResponseEntity<>(userService.getAllInActive(), HttpStatus.OK);
    }
    @GetMapping("/activate/{userId}")
    @ApiOperation("Activate User Account")
    public ResponseEntity<Void> activateUserAccount(@PathVariable("userId") String id) {
        logger.info("Activate User Account{}");   
        User user = userService.get(id);
        user.setActive(Boolean.TRUE);
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @ApiOperation("Deactivate User Account")
    public ResponseEntity<Map<String, Object>> deactivateUser(@ApiParam(name = "id", value = "Id of object to be deleted") @RequestParam("id") String id) {
        logger.info("Deactivating User Account");
        Map<String, Object> response = new HashMap<>();
        try {
            userService.delete(userService.get(id));
            
        } catch (Exception ex) {
            response.put("message", "System error occurred deleting item");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message", "User Account Deactivated");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

   
}
