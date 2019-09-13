package com.totalit.smarthealth.auth.jwt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.totalit.smarthealth.domain.Company;
import com.totalit.smarthealth.domain.User;
import java.io.Serializable;

public class JwtTokenResponse implements Serializable {

	private static final long serialVersionUID = 8317676219297719109L;

	private final String token;
        private User user;
        @JsonIgnoreProperties({ "active", "deleted", "createdById", "uuid", "version", "dateCreated", "dateModified", "modules"})
        private Company company;

        public JwtTokenResponse(String token) {
            this.token = token;
        }

	public JwtTokenResponse(String token, User user) {
		this.token = token;
                this.user = user;
	}

        public JwtTokenResponse(String token, User user, Company company) {
            this.token = token;
            this.user = user;
            this.company = company;
        }

        public Company getCompany() {
            return company;
        }
        

	public String getToken() {
		return this.token;
	}

        public User getUser() {
            return user;
        }
        
}