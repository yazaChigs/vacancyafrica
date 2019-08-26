
package com.totalit.smarthealth.service.impl;
import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.User;
import com.totalit.smarthealth.repository.UserRepo;
import com.totalit.smarthealth.service.UserService;
import com.totalit.smarthealth.util.AppUtil;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author roy
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepo userRepo;

   
    @Override
    public void delete(User t) {
        if (t.getId() == null) {
            throw new IllegalStateException("Item to be deleted is in an inconsistent state");
        }
        t.setActive(Boolean.FALSE);
        userRepo.save(t);
    }

    @Override
    public List<User> getPageable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    


    @Override
    public String getCurrentUsername() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            return null;
        }
        if (authentication.getPrincipal() instanceof String) {
            String principal = (String) authentication.getPrincipal();

            if (principal.compareTo("anonymousUser") != 0) {
                return null;
            }
            return principal;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    

    


    @Override
    public List<User> findByActiveAndDateModified(Boolean active, Date date) {
         return userRepo.findByActiveAndDateModified(active, date);
    }

    @Override
    public User findByUuid(String uuid) {
        return userRepo.findByUuid(uuid);
    }

    @Override
    public List<User> findByActiveAndDateCreated(Boolean active, Date date) {
         return userRepo.findByActiveAndDateCreated(active, date);
    }

    @Override
    public User findByUserName(String name) {
        return userRepo.findByActiveAndUserName(Boolean.TRUE, name);
    }

    @Override
    public User getCurrentUser() {
         String username = getCurrentUsername();
        if (username == null) {
            return null;
        }
        User user = findByUserName(username);
        if (user == null) {
            return null;
        }
        return user;
    }

    @Override
    public List<User> getAllInActive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User changePassword(User t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Optional<User> findUserByResetToken(String resetToken) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<User> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User get(String id) {
        return userRepo.findById(id).get();
    }

    @Override
    public User save(User t) {
         if (t.getId() == null) {
            t.setCreatedBy(getCurrentUser());
            t.setDateCreated(new Date());
            t.setUuid(AppUtil.generateUUID());
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedPassword = encoder.encode(t.getPassword());
            t.setPassword(hashedPassword);
            return userRepo.save(t);
        }
          if(t.getCreatedById()!=null){
               t.setCreatedBy(get(t.getCreatedById()));
           }
        t.setModifiedBy(getCurrentUser());
        t.setDateModified(new Date());
        return userRepo.save(t);
    }

    @Override
    public Boolean checkDuplicate(User current, User old, Company company) {
         if(current.getId() == null){
        return userRepo.existsByActiveAndUserNameIgnoreCaseAndCompany(Boolean.TRUE, current.getUserName(), company);
        }
        old = get(current.getId());
        if(current.getUserName().equalsIgnoreCase(old.getUserName())){
            return Boolean.TRUE;
        }else{
          return userRepo.existsByActiveAndUserNameIgnoreCaseAndCompany(Boolean.TRUE, current.getUserName(), company);        }
    }

    @Override
    public List<User> getByCompany(Company company) {
        return userRepo.findByActiveAndCompany(Boolean.TRUE, company);
    }

    @Override
    public Boolean checkDuplicate(User current, User old) {
         if(current.getId() == null){
        return userRepo.existsByActiveAndUserNameIgnoreCase(Boolean.TRUE, current.getUserName());
        }
        old = get(current.getId());
        if(current.getUserName().equalsIgnoreCase(old.getUserName())){
            return Boolean.TRUE;
        }else{
          return userRepo.existsByActiveAndUserNameIgnoreCase(Boolean.TRUE, current.getUserName());        }
    }
}
