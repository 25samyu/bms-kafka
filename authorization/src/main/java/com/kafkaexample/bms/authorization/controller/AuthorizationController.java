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

@RestController
public class AuthorizationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationController.class);

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private DetailsService detailsService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserCredentials userCredentials) {
		try {
			final UserDetails userDetails = detailsService.loadUserByUsername(userCredentials.getUsername());
			if (userDetails == null)
				return new ResponseEntity<>("Not Accessible", HttpStatus.FORBIDDEN);

			if (userDetails.getPassword().equals(userCredentials.getPassword())) {
				String generatedToken = jwtUtil.generateToken(userDetails);

				return new ResponseEntity<>(generatedToken, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Not Accessible", HttpStatus.FORBIDDEN);
			}
		} catch (Exception e) {

			return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/validate")
	public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
		try {
			if (token == null) {

				return new ResponseEntity<>("Not Accessible", HttpStatus.FORBIDDEN);
			} else {
				String token1 = token.substring(7);
				if (jwtUtil.validateToken(token1)) {

					return new ResponseEntity<>("Accessible", HttpStatus.OK);
				} else {
					return new ResponseEntity<>("Not Accessible", HttpStatus.FORBIDDEN);

				}
			}
		} catch (Exception e) {

			return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
