/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.totalit.smarthealth.domain.util.SaleStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author roy
 */
@Document(collection="sales")
@TypeAlias("sale")
@Getter @Setter @NoArgsConstructor
public class Sale extends BaseCompany{
    @JsonIgnoreProperties({ "active", "deleted", "createdById", "uuid", "version", "dateCreated", "dateModified"})
    @DBRef
    private Customer customer;
    @JsonIgnoreProperties({ "active", "deleted", "createdById", "uuid", "version", "dateCreated", "dateModified"})
    @DBRef
    private Branch branch;
    private Boolean isWalkInCustomer;
    private LocalDateTime timeIniated;
    @Transient
    private String timeInit;
    private SaleStatus saleStatus;
    private String referenceNumber;
    private Long totalQuantity;
    private Double totalDiscount;
    private Double amountBeforeDiscount;
    private Double amountAfterDiscount;
    private Double amountPaid;
    @JsonIgnoreProperties({ "active", "deleted", "createdById", "uuid", "version", "dateCreated", "dateModified"})
    @DBRef
    private List<PaymentReceived> paymentTypes = new ArrayList<>();
    private Double change;
    @JsonIgnoreProperties({ "active", "deleted", "createdById", "uuid", "version", "dateCreated", "dateModified"})
    private Currency currency;
    @JsonIgnoreProperties({ "active", "deleted", "createdById", "uuid", "version", "dateCreated", "dateModified"})
    private Set<InventoryItem> saleItems = new HashSet<>();
}
