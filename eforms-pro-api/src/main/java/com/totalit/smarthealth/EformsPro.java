/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth;

import com.totalit.smarthealth.domain.User;
import com.totalit.smarthealth.domain.UserRole;
import com.totalit.smarthealth.service.UserRoleService;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.service.impl.StorageService;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author kanaz
 */
@SpringBootApplication
@EnableScheduling
public class EformsPro extends SpringBootServletInitializer implements CommandLineRunner {
    
    
    @Resource
    StorageService storageService;
    @Autowired
    private UserRoleService roleService;
    @Autowired
    private UserService userService;
   
     public static void main(String[] args) {
        System.out.println("Current Directory = " + System.getProperty("user.dir"));
        SpringApplication.run(EformsPro.class, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(EformsPro.class);
    }
    @Override
    public void run(String... arg) throws Exception {
        storageService.init();
        roleScript();
        UserRole role = new UserRole();
        UserRole ur = null;
        role.setActive(Boolean.TRUE);
        role.setName("ROLE_GLOBAL");
        role.setDescription("Global role");
        UserRole userRole = roleService.getByName(role.getName());
        if(userRole==null){
            ur = roleService.save(role);
        }else{
            ur = userRole;
        }
        Set<UserRole> roles = new HashSet<>();
        roles.add(ur);
        User user = new User();
        user.setFirstName("Shepherd");
        user.setLastName("Chikomo");
        user.setActive(Boolean.TRUE);
        //user.setAddress("17057 Zengeza 4 Chitungwiza");
        
        user.setUserName("shep@totalit.org");
        user.setPassword("Pass1234");
        user.setUserRoles(roles);
        User u = userService.findByUserName(user.getUserName());
        if(u == null){
            userService.save(user);
        } 
       
    }
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
    private void roleScript(){
        UserRole role1 = new UserRole();
        role1.setActive(Boolean.TRUE);
        role1.setName("ROLE_ADMIN");
        role1.setDescription("Admin  role");       
        UserRole userRole1 = roleService.getByName(role1.getName());
        if(userRole1==null){
         roleService.save(role1);
        }
        UserRole role2 = new UserRole();
        role2.setActive(Boolean.TRUE);
        role2.setName("ROLE_USER");
        role2.setDescription("User  role");       
        UserRole userRole2 = roleService.getByName(role2.getName());
        if(userRole2==null){
            roleService.save(role2);
        }
    }
}
