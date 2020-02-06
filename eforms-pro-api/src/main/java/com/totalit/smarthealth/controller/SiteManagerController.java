/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.controller;

import com.totalit.smarthealth.service.*;
import com.totalit.smarthealth.service.impl.StorageService;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author roy
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/site-manager")
public class SiteManagerController {
    public static final Logger logger = LoggerFactory.getLogger(SiteManagerController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;
     @Autowired
    private UserRoleService roleService;
    @Autowired
    private PermissionService permission;
    @Autowired
    private CategoryService categoryService;
   
     
      @GetMapping("/access")
    @ApiOperation("Count to Companies")
    public ResponseEntity<Map<String, Object>> countCompanies() {
        logger.info("System Access");
        Map<String, Object> response = new HashMap<>();        
        try {
            long roles = roleService.countByActive(Boolean.TRUE);
            response.put("roles", roles);
             long modules = moduleService.countByActive(Boolean.TRUE);
            response.put("modules", modules);
             long permissions = permission.countByActive(Boolean.TRUE);
            response.put("permissions", permissions);
            long categories = categoryService.countByActive(Boolean.TRUE);
            response.put("categories", categories);
            
        } catch (Exception ex) {
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
