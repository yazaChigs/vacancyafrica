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

import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.CreateForm;
import com.totalit.smarthealth.domain.User;

import java.util.List;
import java.util.Optional;


/**
 *
 * @author Roy kanavheti
 */
public interface CreateFormService extends GenericCompanyService<CreateForm>  {

    public CreateForm findByFormName(String formName);

    public CreateForm findByCompanyAndFormName(Company company, String formName);

    
}