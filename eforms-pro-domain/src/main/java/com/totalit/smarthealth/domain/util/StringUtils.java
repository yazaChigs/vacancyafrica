/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.domain.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 *
 * @author tasu
 */
public final class StringUtils {
   
    public static String toCamelCase(String c) {
        if (c == null || c.trim().length() == 0) {
            return c;
        }

        if (!c.trim().contains(" ")) {
           return capitalizeWord(c);
        }

        String[] arrayWords = c.split(" ");
        StringBuilder sb = new StringBuilder();

        for (int i=0; i < arrayWords.length; i++ ) {
            sb.append(capitalizeWord(arrayWords[i].concat(" ")));
        }
        
        return sb.toString();
    }
    
    public static String toCamelCase3(String c) {
        if (c == null || c.trim().length() == 0) {
            return c;
        }

        if (!c.trim().contains("_")) {
           return capitalizeWord(c);
        }

        String[] arrayWords = c.split("_");
        StringBuilder sb = new StringBuilder();

        for (int i=0; i < arrayWords.length; i++ ) {
            sb.append(capitalizeWord(arrayWords[i].concat(" ")));
        }
        
        return sb.toString();
    }
    
    public static String toCamelCase4(String c) {
        if (c == null || c.trim().length() == 0) {
            return c;
        }

        if (!c.trim().contains("_")) {
           return capitalizeAcronym(c);
        }

        String[] arrayWords = c.split("_");
        StringBuilder sb = new StringBuilder();

        for (int i=0; i < arrayWords.length; i++ ) {
            sb.append(capitalizeAcronym(arrayWords[i].concat(" ")));
        }
        
        return sb.toString();
    }

    private static String capitalizeWord(String word) {

        if (word != null && !word.trim().equals("")) {
            return word.substring(0, 1).toUpperCase() + word.substring(1, word.length()).toLowerCase();
        } else {
            return word;
        }
    }
    
    private static String capitalizeAcronym(String word) {

        if (word != null && !word.trim().equals("")) {
            return word.substring(0, 1).toUpperCase() + word.substring(1, word.length()).toUpperCase();
        } else {
            return word;
        }
    }
    
    public static String toCamelCase2(String inputString) {
       String result = "";
       if (inputString.length() == 0 || inputString.isEmpty()) {
           return result;
       }
       char firstChar = inputString.charAt(0);
       char firstCharToUpperCase = Character.toUpperCase(firstChar);
       result = result + firstCharToUpperCase;
       for (int i = 1; i < inputString.length(); i++) {
           char currentChar = inputString.charAt(i);
           char previousChar = inputString.charAt(i - 1);
           if (previousChar == ' ') {
               char currentCharToUpperCase = Character.toUpperCase(currentChar);
               result = result + currentCharToUpperCase;
           } else {
               char currentCharToLowerCase = Character.toLowerCase(currentChar);
               result = result + currentCharToLowerCase;
           }
       }
       return result;
   }
    
    public static String removeWhiteSpacesToLoweCase(String s){
         if (s == null || s.trim().length() == 0) {
            return s;
        }
      return s.replaceAll("\\s+","").toLowerCase();
    }
}
