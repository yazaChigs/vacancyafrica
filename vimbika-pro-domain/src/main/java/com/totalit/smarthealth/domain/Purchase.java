/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.totalit.smarthealth.domain.util.PurchaseStatus;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author roy
 */
@Document(collection = "purchases")
@TypeAlias("purchase")
@Getter
@Setter
@NoArgsConstructor
public class Purchase extends BaseCompany {

    private static final long serialVersionUID = 10039292909291L;
    @JsonIgnoreProperties({ "active", "deleted", "createdById", "uuid", "version", "dateCreated", "dateModified"})
    @DBRef
    private Supplier supplier;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date purchaseDate;

    private PurchaseStatus status;
    private String referenceNumber;
    private String purchaseNote;
    @JsonIgnoreProperties({ "active", "deleted", "createdById", "uuid", "version", "dateCreated", "dateModified"})
    @DBRef
    Set<InventoryItem> purchaseItems = new HashSet<>();
    private Double otherCharge;
    private Double otherChargeTax;
    private Double otherChargeTotal;
    private Double discountOnAll;
    private Double discountOnAllTotal;
    private Double discountType;
    private Double totalAmount;
    private Double purchaseCost;
    private List<Payment> paymentTypes;
    private Double balance;
    private Double amountPaid;
}