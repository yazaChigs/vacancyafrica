/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.controller;

import com.totalit.smarthealth.domain.Branch;
import com.totalit.smarthealth.domain.BranchStock;
import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.InventoryItem;
import com.totalit.smarthealth.domain.Payment;
import com.totalit.smarthealth.domain.Purchase;
import com.totalit.smarthealth.domain.PurchaseItem;
import com.totalit.smarthealth.domain.util.PurchaseStatus;
import com.totalit.smarthealth.service.BranchService;
import com.totalit.smarthealth.service.BranchStockService;
import com.totalit.smarthealth.service.CompanyService;
import com.totalit.smarthealth.service.InventoryItemService;
import com.totalit.smarthealth.service.PaymentService;
import com.totalit.smarthealth.service.PurchaseService;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.util.DateUtil;
import com.totalit.smarthealth.util.EndPointUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author roy
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/purchase")
public class PurchaseController {

    public static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private PurchaseService service;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private InventoryItemService inventoryItemService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private BranchStockService branchStockService;

    @PostMapping("/save")
    @ApiOperation("Persists Purchase to Collection")
    public ResponseEntity<Map<String, Object>> save(@RequestHeader(value = "Company") String company, @RequestBody Purchase purchase) {
        Map<String, Object> response = new HashMap<>();
        Company c = EndPointUtil.getCompany(company);
        purchase.setCompany(c);
        purchase.setPurchaseDate(DateUtil.getDateFromStringApi(purchase.getPurchaseDateString()));
        Branch defaultBranch = branchService.getDefaultBranch(purchase.getCompany());
        try {
            if (purchase.getPaymentTypes() != null) {
                purchase.getPaymentTypes().forEach((t) -> {
                    t.setCompany(c);
                    t.setDateTime(LocalDateTime.now());
                });
                List<Payment> payments = paymentService.saveAll(purchase.getPaymentTypes());
                purchase.setPaymentTypes(payments);
            }
            if (purchase.getIsStockUpdated() == false && purchase.getStatus().equals(PurchaseStatus.RECEIVED)) {
                boolean isUpdated = false;
                if(!defaultBranch.getId().equals(purchase.getBranch().getId())){
                   isUpdated = transferStock(purchase, defaultBranch);// Transfer stock to other branches
 
                }else{
                  isUpdated = updateInventory(purchase.getPurchaseItems()); // Update Inventory items after status is changed to Received  
                }               
                purchase.setIsStockUpdated(isUpdated);
            }
            Purchase s = service.save(purchase);
            response.put("item", s);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "Purchase Saved Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-all")
    @ApiOperation("Returns All Purchases")
    public ResponseEntity<?> getAll(@RequestHeader(value = "Company") String company) {
        logger.info("Retrieving All Purchases By Company{}");
        Company c = EndPointUtil.getCompany(company);
        return new ResponseEntity<>(service.getByCompany(c), HttpStatus.OK);
    }

    @GetMapping("/get-item/{id}")
    @ApiOperation(value = "Returns Purchase of Id passed as parameter", response = Purchase.class)
    public ResponseEntity<Purchase> getItem(@ApiParam(name = "id", value = "Id used to fetch the object") @PathVariable("id") String id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("Set Inactive to Purchase Object")
    public ResponseEntity<Map<String, Object>> delete(@ApiParam(name = "id", value = "id for object to be deleted") @PathVariable("id") String id) {
        logger.info("Set Inactive on Purchase Object");
        Map<String, Object> response = new HashMap<>();
        String itemMessage = "";
        try {
            Purchase purchase = service.get(id);
            purchase.setActive(Boolean.FALSE);
            purchase.setDeleted(Boolean.TRUE);
            service.save(purchase);

        } catch (Exception ex) {
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        itemMessage = itemMessage + " Deleted Successfully";
        response.put("message", itemMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Boolean updateInventory(Set<PurchaseItem> purchaseItems) {
        try {
            for (PurchaseItem purchaseItem : purchaseItems) {
                InventoryItem inventoryItem = purchaseItem.getInventoryItem();
                InventoryItem item = inventoryItemService.get(inventoryItem.getId());
                item.setAvailableItems(purchaseItem.getQuantity() + item.getAvailableItems());
                item.setTax(purchaseItem.getTax());
                item.setPurchasePrice(purchaseItem.getPurchasePrice());
                item.setPriceWithoutTax(purchaseItem.getPriceWithoutTax());
                item.setSellingPrice(purchaseItem.getSellingPrice());
                inventoryItemService.save(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return Boolean.TRUE;
    }

    private Boolean transferStock(Purchase purchase, Branch defaultBranch) {
        try {
            for (PurchaseItem item : purchase.getPurchaseItems()) {
                BranchStock stock = branchStockService.getBranchInventoryItem(purchase.getBranch(), item.getInventoryItem(), Boolean.TRUE);
                long currentStock = stock == null ? 0l : stock.getStock();
                BranchStock branchStock = new BranchStock();
                branchStock.setActive(Boolean.TRUE);
                branchStock.setFrom(defaultBranch);
                branchStock.setBranch(purchase.getBranch());
                branchStock.setItem(item.getInventoryItem());
                branchStock.setTransfered(item.getQuantity());
                branchStock.setStock(item.getQuantity() + currentStock);
                branchStock.setCompanyId(purchase.getCompany().getId());
                branchStockService.save(branchStock);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return Boolean.TRUE;
    }

}
