/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.controller;



import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.User;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.service.impl.StorageService;
import com.totalit.smarthealth.util.EndPointUtil;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private StorageService storageService;
   

    @PostMapping("/save")
    @ApiOperation("Persists User object to the database")
    public ResponseEntity<Map<String, Object>> saveUser(@RequestHeader(value = "Company") String company, @RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        Company c = EndPointUtil.getCompany(company);
        user.setCompany(c);
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
     @GetMapping("/get-all")
    @ApiOperation("Returns All Users")
    public ResponseEntity<?> getAll(@RequestHeader(value = "Company") String company) {
        logger.info("Retrieving All Users By Company{}");
        Company c = EndPointUtil.getCompany(company);
        return new ResponseEntity<>(userService.getByCompany(c), HttpStatus.OK);
    }
     @GetMapping("/get-item/{id}")
    @ApiOperation(value = "Returns User of Id passed as parameter", response = User.class)
    public ResponseEntity<User> getItem(@ApiParam(name = "id", value = "Id used to fetch the object") @PathVariable("id") String id) {
        return new ResponseEntity<>(userService.get(id), HttpStatus.OK);
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

//    @DeleteMapping("/delete")
//    @ApiOperation("Deactivate User Account")
//    public ResponseEntity<Map<String, Object>> deactivateUser(@ApiParam(name = "id", value = "Id of object to be deleted") @RequestParam("id") String id) {
//        logger.info("Deactivating User Account");
//        Map<String, Object> response = new HashMap<>();
//        try {
//            userService.delete(userService.get(id));
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
          User user = userService.get(id);
          user.setActive(Boolean.FALSE);
          user.setDeleted(Boolean.TRUE);
          userService.save(user);
           
        } catch (Exception ex) {
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        itemMessage = itemMessage + " Deleted Successfully"; 
        response.put("message", itemMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/image")
    @ApiOperation("Save Company Logo")
    public ResponseEntity<Map<String, Object>> create(@RequestHeader(value = "Company") String company, @RequestParam("image") MultipartFile file, @RequestParam("id") String id) {
        logger.info("Saving company logo");
        Map<String, Object> response = new HashMap<>();
        try {
//            if(id == null) {User profile = userService.get(id);} else {User profile = userService.get(id);}
            User profile = userService.get(id);

            String[] fileFrags = file.getOriginalFilename().split("\\.");
            String extension = fileFrags[fileFrags.length - 1];
            String fileName = profile.getFirstName()+profile.getLastName().concat(".").concat(extension).toLowerCase();
            String dir = "USER-LOGOS";
            Path path = storageService.createNewDirectory(dir);
            profile.setImage(dir.concat(String.valueOf(File.separatorChar)).concat(fileName));

            storageService.storeFile(file, path, fileName);
            userService.save(profile);
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
            User user = userService.get(companyId);
            String name = user.getImage();
            org.springframework.core.io.Resource file = storageService.loadFile(name);
            if(file != null){
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                        .body(file);
            }
        }
        return null;
    }
    
}
