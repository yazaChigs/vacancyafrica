/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import static com.totalit.smarthealth.controller.UserResource.logger;
import com.totalit.smarthealth.domain.BaseEntity;
import com.totalit.smarthealth.domain.BaseName;
import com.totalit.smarthealth.domain.Module;
import com.totalit.smarthealth.domain.Permission;
import com.totalit.smarthealth.domain.User;
import com.totalit.smarthealth.domain.util.BaseNameType;
import com.totalit.smarthealth.service.GenericNameService;
import com.totalit.smarthealth.service.GenericService;
import com.totalit.smarthealth.service.ModuleService;
import com.totalit.smarthealth.service.PermissionService;
import com.totalit.smarthealth.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author roy
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/base-name")
public class BaseNameController {
    public static final Logger logger = LoggerFactory.getLogger(BaseNameController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private PermissionService permissionService;
    
    
    @PostMapping("/save")
    @ApiOperation("Persists New Generic Name Object to Collection")
    public ResponseEntity<Map<String, Object>> save(@RequestBody String item) {
        JSONObject jsonObj = new JSONObject(item);
        Map<String, Object> response = new HashMap<>();       
        ObjectMapper objectMapper = new ObjectMapper(); 
        String itemMessage = "";
        try {
            String type = jsonObj.optString("type");
            if(type.equalsIgnoreCase(BaseNameType.MODULE.toString())){
                Module module = objectMapper.readValue(item, Module.class); 
                Module m = moduleService.save(module);
                itemMessage = "Module";
                response.put("item", m);              
            }else if(type.equalsIgnoreCase(BaseNameType.PERMISSION.toString())){
                Permission permission = objectMapper.readValue(item, Permission.class);
                Permission p = permissionService.save(permission);
                itemMessage = "Permission";
                response.put("item", p); 
            }
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        itemMessage = itemMessage + " Saved Successfully"; 
        response.put("message", itemMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/get-all/{type}")
    @ApiOperation(value = "Returns BaseName of Type passed as parameter", response = BaseName.class)
    public ResponseEntity<Map<String, Object>>  getAll(@ApiParam(name = "type", value = "type used to fetch the objects") @PathVariable("type") String type) {
        Map<String, Object> response = new HashMap<>();  
        if(type.equalsIgnoreCase(BaseNameType.MODULE.toString())){
            List<Module> list = moduleService.getAll();            
            response.put("list", list);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.put("message", "Incorrect Parameter (type) Passed");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
     @DeleteMapping("/delete/{type}/{id}")
    @ApiOperation("Delete BaseName Object")
    public ResponseEntity<Map<String, Object>> delete(@ApiParam(name = "type", value = "type used to fetch the objects") @PathVariable("type") String type,
            @ApiParam(name = "id", value = "id for object to be deleted") @PathVariable("id") String id) {
        logger.info("Deleting BaseName Object");
        Map<String, Object> response = new HashMap<>();
        String itemMessage = "";
        try {
           if(type.equalsIgnoreCase(BaseNameType.MODULE.toString())){
               delete(moduleService, id);
               itemMessage = "Module";
           }else if(type.equalsIgnoreCase(BaseNameType.PERMISSION.toString())){
               delete(permissionService, id);
               itemMessage = "Permission";
           }
            
        } catch (Exception ex) {
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        itemMessage = itemMessage + " Deleted Successfully"; 
        response.put("message", itemMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    public <T extends GenericNameService<S>, S extends BaseName> void delete(T service, String id) throws Exception{       
         S s = service.get(id);
         s.setActive(Boolean.FALSE);
         s.setDeleted(Boolean.TRUE);
         service.save(s);
    }
    
}
