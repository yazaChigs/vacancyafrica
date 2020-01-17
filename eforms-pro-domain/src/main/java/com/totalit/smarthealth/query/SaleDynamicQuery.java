/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.query;

import com.totalit.smarthealth.domain.Brand;
import com.totalit.smarthealth.domain.Category;
import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.Customer;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author roy
 */
@Getter @Setter @NoArgsConstructor
public class SaleDynamicQuery implements Serializable{
        private static final long serialVersionUID = 1L;
        private Customer customer;
        private Company company;
        private Brand brand;
        private Category category;
        private Date startDate;
        private Date endDate;
        private String startDateString;
        private String endDateString;
        private String itemName;
        private String itemCode;                
}
