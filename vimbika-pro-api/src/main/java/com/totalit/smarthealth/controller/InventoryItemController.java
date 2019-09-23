/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.controller;

import com.totalit.smarthealth.domain.Branch;
import com.totalit.smarthealth.domain.BranchStock;
import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.Currency;
import com.totalit.smarthealth.domain.ProductImage;
import com.totalit.smarthealth.domain.InventoryItem;
import com.totalit.smarthealth.service.BranchService;
import com.totalit.smarthealth.service.BranchStockService;
import com.totalit.smarthealth.service.CompanyService;
import com.totalit.smarthealth.service.CurrencyService;
import com.totalit.smarthealth.service.InventoryItemService;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.service.impl.StorageService;
import com.totalit.smarthealth.util.AppUtil;
import com.totalit.smarthealth.util.EndPointUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author roy
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/inventory")
public class InventoryItemController {
    public static final Logger logger = LoggerFactory.getLogger(InventoryItemController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private InventoryItemService service;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private BranchStockService branchStockService;
     @Autowired
    private StorageService storageService;
      
    
    @PostMapping("/item/save")
    @ApiOperation("Persists InventoryItem to Collection")
    public ResponseEntity<Map<String, Object>> save(@RequestHeader(value = "Company") String company, @RequestBody InventoryItem inventoryItem) {
        Map<String, Object> response = new HashMap<>();
        Company c = EndPointUtil.getCompany(company);
        inventoryItem.setCompany(c);
        boolean exist = false;
        try{
        if (!service.checkDuplicate(inventoryItem, inventoryItem, c)) {
            InventoryItem s = service.save(inventoryItem);
            response.put("item", s);
        } else {
            exist = true;
        }
        }
        catch(Exception ex){
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(exist){
            response.put("duplicate", true);
            return new ResponseEntity<>(response, HttpStatus.OK); 
        } 
        response.put("message", "InventoryItem Saved Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/items/get-all")
    @ApiOperation("Returns All InventoryItems")
    public ResponseEntity<?> getAll(@RequestHeader(value = "Company") String company) {
        logger.info("Retrieving All InventoryItems By Company{}");
        Company c = EndPointUtil.getCompany(company);
        List<InventoryItem> list = service.getByCompany(c);
        Currency currency = currencyService.getBaseCurrency(c);
        if(currency !=null){
            list.forEach(item ->{
                double rate = (100 + item.getProfitMargin())/100;
                if(item.getCurrency() != null){ //Currency is not null for inventory item                    
                    if(!item.getCurrency().getId().equalsIgnoreCase(currency.getId())){ //Currency for this inventory item should not be equal to base currency for it to be calculated                       
                        item.setSellingPrice(AppUtil.roundNumber(item.getPurchasePrice() * rate * currency.getRate()));
                        item.setCurrency(currency);
                    }
                }else { // If Inventory Item Currency is null, calculate selling price using base Currency
                        item.setSellingPrice(AppUtil.roundNumber(item.getPurchasePrice() * rate * currency.getRate()));
                        item.setCurrency(currency);
                    }
                
            });
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @GetMapping("/item/get-item/{id}")
    @ApiOperation(value = "Returns InventoryItem of Id passed as parameter", response = InventoryItem.class)
    public ResponseEntity<InventoryItem> getItem(@RequestHeader(value = "Company") String company, @ApiParam(name = "id", value = "Id used to fetch the object") @PathVariable("id") String id) {
        Company c = EndPointUtil.getCompany(company);
        InventoryItem item = service.get(id);
        Currency currency = currencyService.getBaseCurrency(c);
        if (currency != null) {
            double rate = (100+item.getProfitMargin())/100;
            if (item.getCurrency() != null) {
                if (!item.getCurrency().getId().equalsIgnoreCase(currency.getId())) {
                    item.setSellingPrice(AppUtil.roundNumber(item.getPurchasePrice() * rate * currency.getRate()));
                    item.setCurrency(currency);
                }
            } else{
                item.setSellingPrice(AppUtil.roundNumber(item.getPurchasePrice() * rate * currency.getRate()));
                item.setCurrency(currency);
            }
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }
    @GetMapping("/item/branch/{itemId}/{branchId}")
    @ApiOperation(value = "Returns Branch Stock of Item Id and Branch Id passed as parameter", response = InventoryItem.class)
    public ResponseEntity<Long> getBranchItem(@RequestHeader(value = "Company") String company,
            @ApiParam(name = "id", value = "Item Id used to fetch the object")
            @PathVariable("itemId") String itemId,
            @PathVariable("branchId") String branchId) {
        Company c = EndPointUtil.getCompany(company);
        InventoryItem item = service.get(itemId);
        Branch branch =  branchService.get(branchId);
        List<BranchStock> branchStock = branchStockService.findByBranchAndItem(branch, item);    
        Long totalStock = branchStock.stream().mapToLong(BranchStock::getTransfered).sum();
        return new ResponseEntity<>(totalStock, HttpStatus.OK);
    }
    @GetMapping("/item/branch-stocks")
    @ApiOperation("Returns All InventoryItems")
    public ResponseEntity<?> getAllBranchStock(@RequestHeader(value = "Company") String company) {
        logger.info("Retrieving All Branch Stocks By Company{}");
        Company c = EndPointUtil.getCompany(company);
        List<BranchStock> list = branchStockService.getAll(company);
        Currency currency = currencyService.getBaseCurrency(c);
        if(currency !=null){

            list.forEach(item ->{
                if(item.getItem()!=null){
                double rate = (100+item.getItem().getProfitMargin())/100;
                if(item.getItem().getCurrency() != null){ //Currency is not null for inventory item                    
                    if(!item.getItem().getCurrency().getId().equalsIgnoreCase(currency.getId())){ //Currency for this inventory item should not be equal to base currency for it to be calculated                       
                        item.getItem().setSellingPrice(AppUtil.roundNumber(item.getItem().getPurchasePrice() * rate * currency.getRate()));
                        item.getItem().setCurrency(currency);
                    }
                }else { // If Inventory Item Currency is null, calculate selling price using base Currency
                        item.getItem().setSellingPrice(AppUtil.roundNumber(item.getItem().getPurchasePrice() * rate * currency.getRate()));
                        item.getItem().setCurrency(currency);
                    }
                }  
            });
   
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @PostMapping("/item/branch-stock/save")
    @ApiOperation("Persists InventoryItem to Collection")
    public ResponseEntity<Map<String, Object>> saveBranchStock(@RequestHeader(value = "Company") String company, @RequestBody BranchStock branchStock) {
        Map<String, Object> response = new HashMap<>();

        try{
           InventoryItem inventoryItem = service.get(branchStock.getItem().getId());
           inventoryItem.setAvailableItems(inventoryItem.getAvailableItems() - branchStock.getTransfered());
           InventoryItem in = service.save(inventoryItem);
           response.put("inventory", in);
           branchStock.setCompanyId(company);
           BranchStock stock = branchStockService.save(branchStock);
           response.put("item", stock);
        }
        catch(Exception ex){
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
         
        response.put("message", "Branch Stock Saved Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("/item/delete/{id}")
    @ApiOperation("Set Inactive to InventoryItem Object")
    public ResponseEntity<Map<String, Object>> delete(@ApiParam(name = "id", value = "id for object to be deleted") @PathVariable("id") String id) {
        logger.info("Set Inactive on InventoryItem Object");
        Map<String, Object> response = new HashMap<>();
        String itemMessage = "";
        try {
          InventoryItem inventoryItem = service.get(id);
          inventoryItem.setActive(Boolean.FALSE);
          inventoryItem.setDeleted(Boolean.TRUE);
          service.save(inventoryItem);
           
        } catch (Exception ex) {
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        itemMessage = itemMessage + " Deleted Successfully"; 
        response.put("message", itemMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/image")
    @ApiOperation("Save Inventory Image")
    public ResponseEntity<Map<String, Object>> create(@RequestParam("image") MultipartFile file, @RequestParam("id") String id) {
        logger.info("Saving company logo");
        Map<String, Object> response = new HashMap<>();
 try {
        InventoryItem inventoryItem = service.get(id);
              
        String[] fileFrags = file.getOriginalFilename().split("\\.");
        String extension = fileFrags[fileFrags.length - 1];
        String uuid = AppUtil.generateUUID();
        String fileName = uuid.concat(".").concat(extension).toLowerCase();
        String dir = "VIMBIKA-INVENTORY-IMAGES";
        Path path = storageService.createNewDirectory(dir);
        String name = dir.concat(String.valueOf(File.separatorChar)).concat(fileName);
        ProductImage image = new  ProductImage();
        image.setActive(Boolean.FALSE);
        image.setBig(name);
        image.setSmall(name);
        image.setMedium(name);
        image.setDescription(inventoryItem.getDescription());
        image.setLabel(inventoryItem.getName());
        image.setUrl(name);
        inventoryItem.getProductImages().add(image);       
            storageService.storeFile(file, path, fileName);
            service.save(inventoryItem);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.put("message", "System error occurred saving item " + ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message", "Product Image Saved Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/image")
    @ResponseBody
    public ResponseEntity<org.springframework.core.io.Resource> getFile(@RequestParam("name") String name) {
        if(name != null && !name.equalsIgnoreCase("null")){
              org.springframework.core.io.Resource file = storageService.loadFile(name);
              if(file != null){
              return ResponseEntity.ok()
                      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                      .body(file);
              }
        }
        return null;
    }
}
