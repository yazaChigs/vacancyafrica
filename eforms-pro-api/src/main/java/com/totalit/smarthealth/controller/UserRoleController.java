/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.controller;


import com.totalit.smarthealth.domain.UserRole;
import com.totalit.smarthealth.service.UserRoleService;
import com.totalit.smarthealth.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tasu
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin/user-role")
@Api(description = "User role resource")
public class UserRoleController {

    @Resource
    private UserRoleService service;
    @Resource
    private UserService userService;
    
     @PostMapping("/save")
    @ApiOperation("Persists new facility object to the database")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Item created sucessfully")
        ,
        @ApiResponse(code = 500, message = "System error occurred saving item")
        ,
        @ApiResponse(code = 400, message = "Usually a validation error. The message will largely depend on the failed validations")
    })
    public ResponseEntity<Map<String, Object>> create(@RequestBody UserRole item) {
        Map<String, Object> response = new HashMap<>();
        if (!response.isEmpty()) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            UserRole role  = service.save(item);
            response.put("role", role);
        } catch (Exception ex) {
            response.put("message", "System error occurred saving item");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message", "Item created sucessfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/get-all")
    @ApiOperation("Returns all active user roles")
    public List<UserRole> getAll() {
        return service.getAll();
    }
    
    
   
    @GetMapping("/get-user-roles")
    @ApiOperation("Returns logged in user's granted authorities")
    public List<String> getGrantedAuthorities(){
        List<String> roles = new ArrayList<>();
        userService.getCurrentUser().getUserRoles().forEach(role ->{
            roles.add(role.getName());
        });
        return roles;
    }

    @GetMapping("/get-item")
    @ApiOperation(value = "Returns user role of id passed as parameter", response = UserRole.class)
    public UserRole getItem(@ApiParam(name = "id", value = "Id used to fetch the object") @RequestParam("id") String id) {
        return service.get(id);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "Deactivate user role of id passed as parameter")
    @ApiResponses({
        @ApiResponse(code = 500, message = "System error occurred deleting item")
        ,
        @ApiResponse(code = 200, message = "Item deleted sucessfully")
    })
    public ResponseEntity<Map<String, Object>> delete(@ApiParam(name = "id", value = "Id of object to be deleted") @RequestParam("id") String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            service.delete(service.get(id));
        } catch (Exception ex) {
            response.put("message", "System error occurred deleting item");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message", "Item deleted sucessfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

   
}
