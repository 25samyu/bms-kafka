package com.kafkaexample.bms.authorization.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.kafkaexample.bms.authorization.model.UserCredentials;
import com.kafkaexample.bms.authorization.service.DetailsService;
import com.kafkaexample.bms.authorization.service.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class AuthorizationController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private DetailsService detailsService;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserCredentials userCredentials) {
		logger.info("Start");
		try {
			final UserDetails userDetails = detailsService.loadUserByUsername(userCredentials.getUsername());
			if (userDetails == null) {
				logger.info("End - User not found - Unauthorized");
				return new ResponseEntity<>("Not Accessible", HttpStatus.FORBIDDEN);
			}
			if (userDetails.getPassword().equals(userCredentials.getPassword())) {
				String generatedToken = jwtUtil.generateToken(userDetails);
				logger.info("End - Success");
				return new ResponseEntity<>(generatedToken, HttpStatus.OK);
			} else {
				logger.info("End - Wrong password - Unauthorized");
				return new ResponseEntity<>("Not Accessible", HttpStatus.FORBIDDEN);
			}
		} catch (Exception e) {
			logger.error("End - Exception");
			return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/validate")
	public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token) {
		logger.info("Start");
		if (token.length() < 10) {
			logger.info("End - Token less than substring index - Unauthorized");
			return new ResponseEntity<>("Not Accessible", HttpStatus.FORBIDDEN);
		} else {
			String token1 = token.substring(7);
			if (jwtUtil.validateToken(token1)) {
				logger.info("End - Success");
				return new ResponseEntity<>("Accessible", HttpStatus.OK);
			} else {
				logger.info("End - Unauthorized");
				return new ResponseEntity<>("Not Accessible", HttpStatus.FORBIDDEN);

			}
		}

	}

}
