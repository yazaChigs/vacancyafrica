/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.service;

import com.totalit.smarthealth.domain.Permission;

/**
 *
 * @author roy
 */
public interface PermissionService extends GenericNameService<Permission> {
    Long countByActive(Boolean active);
}
