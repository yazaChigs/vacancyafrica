/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.controller;

import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.InventoryItem;
import com.totalit.smarthealth.service.CompanyService;
import com.totalit.smarthealth.service.InventoryItemService;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.util.EndPointUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.HashMap;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author roy
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/inventory")
public class InventoryItemController {
    public static final Logger logger = LoggerFactory.getLogger(InventoryItemController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private InventoryItemService service;
    @Autowired
    private CompanyService companyService;
    
    @PostMapping("/item/save")
    @ApiOperation("Persists InventoryItem to Collection")
    public ResponseEntity<Map<String, Object>> save(@RequestHeader(value = "Company") String company, @RequestBody InventoryItem inventoryItem) {
        Map<String, Object> response = new HashMap<>();
        Company c = EndPointUtil.getCompany(company);
        inventoryItem.setCompany(c);
        boolean exist = false;
        try{
        if (!service.checkDuplicate(inventoryItem, inventoryItem, c)) {
            InventoryItem s = service.save(inventoryItem);
            response.put("item", s);
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
        response.put("message", "InventoryItem Saved Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/items/get-all")
    @ApiOperation("Returns All InventoryItems")
    public ResponseEntity<?> getAll(@RequestHeader(value = "Company") String company) {
        logger.info("Retrieving All InventoryItems By Company{}");
        Company c = EndPointUtil.getCompany(company);
        return new ResponseEntity<>(service.getByCompany(c), HttpStatus.OK);
    }
    @GetMapping("/item/get-item/{id}")
    @ApiOperation(value = "Returns InventoryItem of Id passed as parameter", response = InventoryItem.class)
    public ResponseEntity<InventoryItem> getItem(@ApiParam(name = "id", value = "Id used to fetch the object") @PathVariable("id") String id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }
    
    @DeleteMapping("/item/delete/{id}")
    @ApiOperation("Set Inactive to InventoryItem Object")
    public ResponseEntity<Map<String, Object>> delete(@ApiParam(name = "id", value = "id for object to be deleted") @PathVariable("id") String id) {
        logger.info("Set Inactive on InventoryItem Object");
        Map<String, Object> response = new HashMap<>();
        String itemMessage = "";
        try {
          InventoryItem inventoryItem = service.get(id);
          inventoryItem.setActive(Boolean.FALSE);
          inventoryItem.setDeleted(Boolean.TRUE);
          service.save(inventoryItem);
           
        } catch (Exception ex) {
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        itemMessage = itemMessage + " Deleted Successfully"; 
        response.put("message", itemMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
