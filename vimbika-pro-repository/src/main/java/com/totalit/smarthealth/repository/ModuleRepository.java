/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.repository;

import com.totalit.smarthealth.domain.Module;
import org.springframework.stereotype.Repository;

/**
 *
 * @author roy
 */
@Repository
public interface ModuleRepository extends AbstractNameDescRepo<Module, String>{
    
}
