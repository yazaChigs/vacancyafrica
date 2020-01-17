/*
 * Copyright 2015 Edward Zengeni.
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
package com.totalit.smarthealth.repository;


import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.User;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Roy kanavheti
 */
@Repository
public interface UserRepo extends AbstractCompanyRepository<User, String> {
    public User findByActiveAndUserName(Boolean active, String userName);
    public Boolean existsByActiveAndUserNameIgnoreCase(Boolean active, String userName);
    public Boolean existsByActiveAndUserNameIgnoreCaseAndCompany(Boolean active, String userName, Company company);
}
