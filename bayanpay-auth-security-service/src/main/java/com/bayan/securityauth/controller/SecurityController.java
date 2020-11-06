package com.bayan.securityauth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bayan.securityauth.jwt.util.JwtUtil;
import com.bayan.securityauth.model.AuthenticationRequest;
import com.bayan.securityauth.model.AuthenticationResponse;
import com.bayan.securityauth.model.ErrorResponse;
import com.bayan.securityauth.service.MovitUserDetailsService;

@RestController

//@CrossOrigin(origins = "http://localhost:8500")
public class SecurityController {

	final static Logger logger = LoggerFactory.getLogger(SecurityController.class);
	Integer t;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private MovitUserDetailsService userDetailsService;

	@GetMapping("/hello")
	public String firstPage() {

		return "Hello World";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/getAuthToken")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {
		try {
			logger.info("Its my first log");
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			logger.info("Its my first log", e);
			ErrorResponse errorResponse = new ErrorResponse("100", "Invalid User");
			return new ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED);
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		AuthenticationResponse generateToken = jwtTokenUtil.generateToken(userDetails.getUsername(),
				authenticationRequest.getPassword(), authenticationRequest.getClient_id(),
				authenticationRequest.getClient_secret(), authenticationRequest.getGrant_type(),
				authenticationRequest.getScope());
		return ResponseEntity.ok(generateToken);
	}

	@PostMapping("/validateToken")
	public ResponseEntity<?> getCurrent() throws Exception {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String authenticatedUserName = authentication.getName();
			if (authenticatedUserName.equals("anonymousUser")) {
				ErrorResponse errorResponse = new ErrorResponse("100", "Invalid User");
				return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
			} else {
				return new ResponseEntity<UserDetails>((UserDetails) authentication.getPrincipal(), null,
						HttpStatus.OK);
			}
		} catch (Exception e) {
			ErrorResponse errorResponse = new ErrorResponse("100", "Invalid User");
			return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
		}
	}
}
