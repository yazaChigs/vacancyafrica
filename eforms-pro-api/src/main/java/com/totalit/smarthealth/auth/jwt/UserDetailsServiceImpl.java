/*
 * Copyright 2015 Judge Muzinda.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.totalit.smarthealth.auth.jwt;


import com.totalit.smarthealth.domain.User;
import com.totalit.smarthealth.domain.UserRole;
import com.totalit.smarthealth.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Judge Muzinda
 */
@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

    final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserService userService;
    @Autowired
    HttpServletRequest request;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String userName)
            throws UsernameNotFoundException {
        org.springframework.security.core.userdetails.UserDetails userDetails = null;
        User user = userService.findByUserName(userName);
        if (user != null) {
            String password = user.getPassword();
            Set<UserRole> roles = user.getUserRoles();
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (UserRole role : roles) {
                String roleName = role.getName();
                authorities.add(new SimpleGrantedAuthority(roleName));
            }
            userDetails = new org.springframework.security.core.userdetails.User(userName, password, authorities);

        } else {
            // If username not found, throw exception
            logger.info("User " + userName + " not found");
            throw new UsernameNotFoundException("User " + userName + " not found");
        }
        logger.info("Loading user record for user name: {}", userName);
        return userDetails;
    }

}
