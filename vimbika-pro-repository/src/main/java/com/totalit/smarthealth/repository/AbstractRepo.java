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
package com.totalit.smarthealth.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * @author Roy Kanavheti
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface AbstractRepo<T , ID extends Serializable> extends MongoRepository<T, ID> {
    
    @Override
    public List<T> findAll();
    
    public List<T> findByActive(Boolean active);
    public List<T> findByActiveAndDateModified(Boolean active, Date date);
    public List<T> findByActiveAndDateCreated(Boolean active, Date date);
    public T findByUuid(String uuid);
}