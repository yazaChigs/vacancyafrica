/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.domain;

import com.totalit.smarthealth.domain.util.ItemType;
import com.totalit.smarthealth.domain.util.TaxType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    @DBRef
    private Category category;
    @DBRef
    private Unit unit;
    @DBRef
    private Tax tax;
    private Long alertQuantity;
    private String sku;
    private String image;
    private String itemCode;
    private Double priceWithoutTax;
    private Double purchasePrice;
    private TaxType taxType;
    private ItemType itemType;
    private Double profitMargin;
    private Double sellingPrice;
    private String barCode;
    private Long availableItems; 
    
}
