/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 *
 * @author roy
 */
@Getter @Setter @NoArgsConstructor
public class PurchaseItem implements Serializable{
    @JsonIgnoreProperties({ "active", "deleted", "createdById", "uuid", "version", "dateCreated", "dateModified", "tax", "total", "quantity"})
    @DBRef
    private InventoryItem inventoryItem;
    private Long quantity;
    private Double purchasePrice;
    @JsonIgnoreProperties({ "active", "deleted", "createdById", "uuid", "version", "dateCreated", "dateModified"})
    @DBRef
    private Tax tax;
    private Double taxAmount;
    private Double discount;
    private Double unitCost;
    private Double totalAmount;
    private Long profitMargin;
    private Double unitSalePrice;
}
