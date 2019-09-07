/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author roy
 */
@Document(collection="expenses")
@TypeAlias("expense")
@Getter @Setter @NoArgsConstructor
public class Expense extends BaseCompany{
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date expenseDate;
    @Transient
    private String expenseDateString;
    @JsonIgnoreProperties({ "active", "deleted", "createdById", "uuid", "version", "dateCreated", "dateModified"})
    @DBRef
    private ExpenseCategory expenseCategory;
    private String expenseFor;
    private String note;
    private String referenceNumber;
    private Double amount;
    @JsonIgnoreProperties({ "active", "deleted", "createdById", "uuid", "version", "dateCreated", "dateModified"})
    private Set<Payment> paymentTypes  = new HashSet<>(); 
}
