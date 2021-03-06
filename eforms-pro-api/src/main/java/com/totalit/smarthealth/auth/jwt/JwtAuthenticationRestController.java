package com.totalit.smarthealth.auth.jwt;

import com.totalit.smarthealth.domain.User;
import com.totalit.smarthealth.service.UserService;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class JwtAuthenticationRestController {
    final Logger logger = LoggerFactory.getLogger(JwtAuthenticationRestController.class);

	@Value("${jwt.http.request.header}")
	private String tokenHeader;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;
        
        @Autowired
        private UserService userService;

	@RequestMapping(value = "${jwt.get.token.uri}", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtTokenRequest authenticationRequest)
			throws AuthenticationException {
		authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassword());
		final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(authenticationRequest.getUserName());
		final String token = jwtTokenUtil.generateToken(userDetails);
                final User user = userService.findByUserName(authenticationRequest.getUserName());
                if(user != null){
                    if(user.getCompany() !=null){
                        return ResponseEntity.ok(new JwtTokenResponse(token, user, user.getCompany()));
                    }
                    return ResponseEntity.ok(new JwtTokenResponse(token, user));
                }
		return ResponseEntity.ok(new JwtTokenResponse(token));
	}
       

	@RequestMapping(value = "${jwt.refresh.token.uri}", method = RequestMethod.GET)
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
		String authToken = request.getHeader(tokenHeader);
		final String token = authToken.substring(7);
		//String username = jwtTokenUtil.getUsernameFromToken(token);
		//JwtUserDetails user = (JwtUserDetails) jwtInMemoryUserDetailsService.loadUserByUsername(username);

		if (jwtTokenUtil.canTokenBeRefreshed(token)) {
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			return ResponseEntity.ok(new JwtTokenResponse(refreshedToken));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@ExceptionHandler({ AuthenticationException.class })
	public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	private void authenticate(String username, String password) {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new AuthenticationException("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new AuthenticationException("INVALID_CREDENTIALS", e);
		}
	}
}
