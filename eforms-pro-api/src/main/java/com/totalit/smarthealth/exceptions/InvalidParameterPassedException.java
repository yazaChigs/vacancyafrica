/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.exceptions;

/**
 *
 * @author roy
 */
public class InvalidParameterPassedException extends RuntimeException{
     public InvalidParameterPassedException(String parameter) {
        super("Invalid Parameter : " + parameter);
    }
}