package com.totalit.smarthealth.auth.jwt;

import com.totalit.smarthealth.domain.User;
import java.io.Serializable;

public class JwtTokenResponse implements Serializable {

	private static final long serialVersionUID = 8317676219297719109L;

	private final String token;
        private User user;

        public JwtTokenResponse(String token) {
            this.token = token;
        }

	public JwtTokenResponse(String token, User user) {
		this.token = token;
                this.user = user;
	}

	public String getToken() {
		return this.token;
	}

        public User getUser() {
            return user;
        }
        
}