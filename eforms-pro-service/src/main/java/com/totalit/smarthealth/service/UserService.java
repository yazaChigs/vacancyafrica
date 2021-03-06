/*
 * Copyright 2014 Judge Muzinda.
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
package com.totalit.smarthealth.service;
import com.totalit.smarthealth.domain.User;
import java.util.List;
import java.util.Optional;


/**
 *
 * @author Roy kanavheti
 */
public interface UserService extends GenericCompanyService<User> {

    public User findByUserName(String name);

    public String getCurrentUsername();

    public User getCurrentUser();
     List<User> getAllInActive();
    /*
     change pass
    */
    public User changePassword(User t);
    public Optional<User> findUserByResetToken(String resetToken);
    
}