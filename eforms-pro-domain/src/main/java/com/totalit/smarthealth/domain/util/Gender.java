/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.domain.util;

/**
 *
 * @author Roy
 */
public enum Gender {
    MALE(1), FEMALE(2), OTHER(3);

    private final Integer code;

    private Gender(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static Gender get(Integer code) {
        switch (code) {
            case 1:
                return MALE;
            case 2:
                return FEMALE;
                case 3:
                return OTHER;
            default:
                throw new IllegalArgumentException("Unknown parameter passed to method expected {1-3} and receieved :" + code);
        }
    }

    public static Gender get(String code) {
        switch (code) {
            case "male":
                return MALE;
            case "female":
                return FEMALE;
                case "other":
                return OTHER;
            case "":
                return null;
            default:
                throw new IllegalArgumentException("Unknown parameter passed to method expected {1-3} and receieved :" + code);
        }
    }

    public String getName() {
        return StringUtils.toCamelCase3(super.name());
    }

    private static final Gender[] VALUES = {Gender.MALE, Gender.FEMALE, Gender.OTHER};

    public static Gender[] getItems() {
        return VALUES.clone();
    }
}
