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
public class VimbikaPro  extends SpringBootServletInitializer implements CommandLineRunner {
    
    
    @Resource
    StorageService storageService;
    @Autowired
    private UserRoleService roleService;
    @Autowired
    private UserService userService;
   
     public static void main(String[] args) {
        SpringApplication.run(VimbikaPro.class, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(VimbikaPro.class);
    }
    @Override
    public void run(String... arg) throws Exception {
       // storageService.init();
//        UserRole role = new UserRole();
//        role.setActive(Boolean.TRUE);
//        role.setName("ROLE_USER");
//        role.setDeleted(Boolean.FALSE);
//        roleService.save(role);
UserRole role = new UserRole();
        UserRole ur = null;
        role.setActive(Boolean.TRUE);
        role.setName("ROLE_ADMIN");
        role.setDescription("Admin role");
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
        
        user.setUserName("shepherd@totalit.org");
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
    
}
