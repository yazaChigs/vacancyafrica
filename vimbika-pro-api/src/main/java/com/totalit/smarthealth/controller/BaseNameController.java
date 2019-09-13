/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalit.smarthealth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.totalit.smarthealth.domain.BaseName;
import com.totalit.smarthealth.domain.BaseNameCompany;
import com.totalit.smarthealth.domain.Brand;
import com.totalit.smarthealth.domain.Category;
import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.ExpenseCategory;
import com.totalit.smarthealth.domain.Module;
import com.totalit.smarthealth.domain.PaymentType;
import com.totalit.smarthealth.domain.Permission;
import com.totalit.smarthealth.domain.Unit;
import com.totalit.smarthealth.domain.UserRole;
import com.totalit.smarthealth.domain.util.BaseNameType;
import com.totalit.smarthealth.exceptions.InvalidParameterPassedException;
import com.totalit.smarthealth.service.BrandService;
import com.totalit.smarthealth.service.CategoryService;
import com.totalit.smarthealth.service.ExpenseCategoryService;
import com.totalit.smarthealth.service.GenericNameCompanyService;
import com.totalit.smarthealth.service.GenericNameService;
import com.totalit.smarthealth.service.ModuleService;
import com.totalit.smarthealth.service.PaymentTypeService;
import com.totalit.smarthealth.service.PermissionService;
import com.totalit.smarthealth.service.UnitService;
import com.totalit.smarthealth.service.UserRoleService;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.util.EndPointUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
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
@RequestMapping("/api/base-name")
public class BaseNameController {
    public static final Logger logger = LoggerFactory.getLogger(BaseNameController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserRoleService roleService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private CategoryService categoryService;
     @Autowired
    private BrandService brandService;
    @Autowired
    private PaymentTypeService paymentTypeService;
    @Autowired
    private ExpenseCategoryService expenseCategoryService;
    
    @PostMapping("/save")
    @ApiOperation("Persists New Generic Name Object to Collection")
    public ResponseEntity<Map<String, Object>> save(@RequestHeader(value = "Company") String company, @RequestBody String item) {
        JSONObject jsonObj = new JSONObject(item);
        Map<String, Object> response = new HashMap<>();       
        ObjectMapper objectMapper = new ObjectMapper(); 
        Company c = EndPointUtil.getCompany(company);
        String itemMessage = "";
        boolean exist = false;
        try {
            String type = jsonObj.optString("type");
            if(type.equalsIgnoreCase(BaseNameType.MODULE.toString())){
                Module module = objectMapper.readValue(item, Module.class); 
                if(!moduleService.checkDuplicate(module, module)){
                    Module m = moduleService.save(module);
                    itemMessage = "Module";
                    response.put("item", m);  
                }else{
                    exist = true;
                }
                            
            }else if(type.equalsIgnoreCase(BaseNameType.PERMISSION.toString())){
                Permission permission = objectMapper.readValue(item, Permission.class);
                if(!permissionService.checkDuplicate(permission, permission)){
                Permission p = permissionService.save(permission);
                itemMessage = "Permission";
                response.put("item", p); 
                }else{
                    exist = true;
                }
            }
            else if(type.equalsIgnoreCase(BaseNameType.ROLE.toString())){
                UserRole role = objectMapper.readValue(item, UserRole.class);
                if(!roleService.checkDuplicate(role, role)){
                UserRole r = roleService.save(role);
                itemMessage = "Role";
                response.put("item", r); 
                }else{
                    exist = true;
                }
            }
            else if(type.equalsIgnoreCase(BaseNameType.UNIT.toString())){
                Unit unit = objectMapper.readValue(item, Unit.class);
                if(!unitService.checkDuplicate(unit, unit, c)){
                    unit.setCompany(c);
                Unit r = unitService.save(unit);
                itemMessage = "Unit";
                response.put("item", r); 
                }else{
                    exist = true;
                }
            }
            else if(type.equalsIgnoreCase(BaseNameType.CATEGORY.toString())){
                Category category = objectMapper.readValue(item, Category.class);
                if(!categoryService.checkDuplicate(category, category, c)){
                    category.setCompany(c);
                Category r = categoryService.save(category);
                itemMessage = "Category";
                response.put("item", r); 
                }else{
                    exist = true;
                }
            }
             else if(type.equalsIgnoreCase(BaseNameType.PAYMENT_TYPE.toString())){
                PaymentType paymentType = objectMapper.readValue(item, PaymentType.class);
                if(!paymentTypeService.checkDuplicate(paymentType, paymentType, c)){
                    paymentType.setCompany(c);
                PaymentType r = paymentTypeService.save(paymentType);
                itemMessage = "Payment Type";
                response.put("item", r); 
                }else{
                    exist = true;
                }
            }
             else if(type.equalsIgnoreCase(BaseNameType.EXPENSE_CATEGORY.toString())){
                ExpenseCategory expenseCategory = objectMapper.readValue(item, ExpenseCategory.class);
                if(!expenseCategoryService.checkDuplicate(expenseCategory, expenseCategory, c)){
                    expenseCategory.setCompany(c);
                ExpenseCategory r = expenseCategoryService.save(expenseCategory);
                itemMessage = "Expense Category";
                response.put("item", r); 
                }else{
                    exist = true;
                }
            }
            else if(type.equalsIgnoreCase(BaseNameType.BRAND.toString())){
                Brand brand = objectMapper.readValue(item, Brand.class);
                if(!brandService.checkDuplicate(brand, brand, c)){
                    brand.setCompany(c);
                Brand r = brandService.save(brand);
                itemMessage = "Brand";
                response.put("item", r); 
                }else{
                    exist = true;
                }
            }
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        if(exist){
            response.put("duplicate", true);
            return new ResponseEntity<>(response, HttpStatus.OK); 
        }
        itemMessage = itemMessage + " Saved Successfully"; 
        response.put("message", itemMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/get-all/{type}")
    @ApiOperation(value = "Returns BaseName of Type passed as parameter", response = BaseName.class)
    public ResponseEntity<Map<String, Object>>  getAll(@RequestHeader(value = "Company") String company,
            @ApiParam(name = "type", value = "type used to fetch the objects") @PathVariable("type") String type) {
        Map<String, Object> response = new HashMap<>();  
        Company c = EndPointUtil.getCompany(company);
        if(type.equalsIgnoreCase(BaseNameType.MODULE.toString())){
            List<Module> list = moduleService.getAll();            
            response.put("list", list);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else if(type.equalsIgnoreCase(BaseNameType.ROLE.toString())){
           List<UserRole> list = roleService.getAll();            
           response.put("list", list);
           return new ResponseEntity<>(response, HttpStatus.OK); 
        }
        else if(type.equalsIgnoreCase(BaseNameType.UNIT.toString())){
           List<Unit> list = unitService.getAll(c);            
           response.put("list", list);
           return new ResponseEntity<>(response, HttpStatus.OK); 
        }
        else if(type.equalsIgnoreCase(BaseNameType.CATEGORY.toString())){
           List<Category> list = categoryService.getAll(c);            
           response.put("list", list);
           return new ResponseEntity<>(response, HttpStatus.OK); 
        }
        else if(type.equalsIgnoreCase(BaseNameType.PAYMENT_TYPE.toString())){
           List<PaymentType> list = paymentTypeService.getAll(c);            
           response.put("list", list);
           return new ResponseEntity<>(response, HttpStatus.OK); 
        }
        else if(type.equalsIgnoreCase(BaseNameType.EXPENSE_CATEGORY.toString())){
           List<ExpenseCategory> list = expenseCategoryService.getAll(c);            
           response.put("list", list);
           return new ResponseEntity<>(response, HttpStatus.OK); 
        }
        else if(type.equalsIgnoreCase(BaseNameType.BRAND.toString())){
           List<Brand> list = brandService.getAll(c);            
           response.put("list", list);
           return new ResponseEntity<>(response, HttpStatus.OK); 
        }
        response.put("message", "Incorrect Parameter (type) Passed");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{type}/{id}")
    @ApiOperation("Delete BaseName Object")
    public ResponseEntity<Map<String, Object>> delete(@ApiParam(name = "type", value = "type used to fetch the objects") @PathVariable("type") String type,
            @ApiParam(name = "id", value = "id for object to be deleted") @PathVariable("id") String id) {
        logger.info("Deleting BaseName Object");
        Map<String, Object> response = new HashMap<>();
        String itemMessage = "";
        try {
           if(type.equalsIgnoreCase(BaseNameType.MODULE.toString())){
               delete(moduleService, id);
               itemMessage = "Module";
           }else if(type.equalsIgnoreCase(BaseNameType.PERMISSION.toString())){
               delete(permissionService, id);
               itemMessage = "Permission";
           }
           else if(type.equalsIgnoreCase(BaseNameType.ROLE.toString())){
               delete(roleService, id);
               itemMessage = "Role";
           }
           else if(type.equalsIgnoreCase(BaseNameType.UNIT.toString())){
               delete(unitService, id);
               itemMessage = "Unit";
           }
           else if(type.equalsIgnoreCase(BaseNameType.CATEGORY.toString())){
               delete(categoryService, id);
               itemMessage = "Category";
           }
           else if(type.equalsIgnoreCase(BaseNameType.PAYMENT_TYPE.toString())){
               delete(paymentTypeService, id);
               itemMessage = "Payment Type";
           }
           else if(type.equalsIgnoreCase(BaseNameType.EXPENSE_CATEGORY.toString())){
               delete(expenseCategoryService, id);
               itemMessage = "Expense Category";
           }
           else if(type.equalsIgnoreCase(BaseNameType.BRAND.toString())){
               delete(brandService, id);
               itemMessage = "Brand";
           }
            
        } catch (Exception ex) {
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        itemMessage = itemMessage + " Deleted Successfully"; 
        response.put("message", itemMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/getByName/{type}/{name}")
    @ApiOperation("Get BaseName Object")
    public ResponseEntity<?> getByName(@RequestHeader(value = "Company") String company,
            @ApiParam(name = "type", value = "type used to fetch the objects") @PathVariable("type") String type,
            @ApiParam(name = "name", value = "name for object to be retrieved") @PathVariable("name") String name) {
        Company c = EndPointUtil.getCompany(company);
         if(type.equalsIgnoreCase(BaseNameType.PAYMENT_TYPE.toString())){
               PaymentType paymentType = paymentTypeService.getByNameAndCompany(name, c);
               return new ResponseEntity<>(paymentType, HttpStatus.OK);
           }
         else{
             throw new InvalidParameterPassedException(type);
         }
        
    }
    public <T extends GenericNameService<S>, S extends BaseName> void delete(T service, String id) throws Exception{       
         S s = service.get(id);
         s.setActive(Boolean.FALSE);
         s.setDeleted(Boolean.TRUE);
         service.save(s);
    }
    public <T extends GenericNameCompanyService<S>, S extends BaseNameCompany> void delete(T service, String id) throws Exception{       
         S s = service.get(id);
         s.setActive(Boolean.FALSE);
         s.setDeleted(Boolean.TRUE);
         service.save(s);
    }
    
    
}
