/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.util;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author roy
 */
public class AppUtil {
    public static String getCustomerId(int id){
        String companyId = zeroAppend(id);
        String[] days = {"", "NM", "OL", "PK", "QJ", "RI", "SH", "TG",
                             "UF", "VE", "WD", "XC", "YB", "ZA", "AZ",
                             "BY", "CX", "DW", "EV", "FU", "GT", "HS",
                             "IR", "JQ", "KP", "LO", "MN", "ZB", "YC",
                             "XD", "WE", "VF"};
        String[] months = {"","Z", "Y", "X","W", "V", "U","T", "S", "R","Q", "P", "O"};
       int currentDay = DateUtil.getCurrentDay();
       int currentMonth = DateUtil.getCurrentMonth()+1;
       StringBuilder sb = new StringBuilder();
       sb.append(days[currentDay]);
       sb.append(months[currentMonth]);
       sb.append(companyId);
        return sb.toString();
    }
    public static String zeroAppend(int number){
         String num  = String.valueOf(number);
         StringBuilder sb = new StringBuilder();
         if(num.length()==1){
            sb.append("0");
            sb.append(num);
         }
         else{
             sb.append(num);
         }
         return sb.toString();
    }
     
     public static String recordId(String customerId, Long id){
        String[] months = {"","A", "B", "C","D", "E", "F","G", "H", "I","J", "K", "L"}; 
        int month = DateUtil.getCurrentMonth()+1;
        int currentDay = DateUtil.getCurrentDay();
        StringBuilder sb = new StringBuilder();
        sb.append(customerId);
        sb.append(getYear());
        sb.append(months[month]);
        sb.append(zeroAppend(currentDay));
        sb.append(zeroAppend(id.intValue()));
        return sb.toString();
        
        
    }
    static int getYear(){
        int yr = DateUtil.getYearFromDate(new Date());
        int year = yr - 2000;
        return year;
    }
    public static String generateUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
