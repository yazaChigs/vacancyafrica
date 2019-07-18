/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.domain.util;

/**
 *
 * @author kanaz
 */
public enum Title {
     DR(1), MR(2), MRS(3), GP(4), MISS(5), PROF(6), PHD(7);

    private final Integer code;
    
    private String name;

    Title(Integer code) {
        this.code = code;
    }

    public static Title get(Integer code) {
        for (Title  item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Uknown parameter passed to method:");
    }

    public Integer getCode() {
        return code;
    }
    
    public String getName(){
        return StringUtils.toCamelCase3(super.name());
    }

    public void setName(String name) {
        this.name = name;
    }
}
