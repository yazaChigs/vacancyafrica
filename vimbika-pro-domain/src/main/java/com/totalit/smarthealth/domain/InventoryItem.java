/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.totalit.smarthealth.domain.util.ItemType;
import com.totalit.smarthealth.domain.util.TaxType;
import java.util.ArrayList;
import java.util.List;
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
@Document(collection="inventory_items")
@TypeAlias("inventoryitem")
@Getter @Setter @NoArgsConstructor
public class InventoryItem extends BaseCompany{
    private String name;
    private String description;
    @JsonIgnoreProperties({ "active", "deleted", "createdById", "uuid", "version", "dateCreated", "dateModified"})
    @DBRef
    private Category category;
     @JsonIgnoreProperties({ "active", "deleted", "createdById", "uuid", "version", "dateCreated", "dateModified"})
    @DBRef
    private Brand brand;
    @JsonIgnoreProperties({ "active", "deleted", "createdById", "uuid", "version", "dateCreated", "dateModified"})
    @DBRef
    private Unit unit;
    @JsonIgnoreProperties({ "active", "deleted", "createdById", "uuid", "version", "dateCreated", "dateModified"})
    @DBRef
    private Currency currency;    
    @JsonIgnoreProperties({ "active", "deleted", "createdById", "uuid", "version", "dateCreated", "dateModified"})
    @DBRef
    private Tax tax;
    private Long alertQuantity;
    private String itemCode;
    private Double priceWithoutTax;
    private Double purchasePrice;
    private TaxType taxType;
    private ItemType itemType;
    private Double profitMargin;
    private Double sellingPrice;
    private String barCode;
    private Long availableItems; 
    @Transient
    private Supplier supplier;
    
    //transient objects for a sale
    private Long quantity;
    private double total;
    private List<ProductImage> productImages = new ArrayList<>();
    
}
