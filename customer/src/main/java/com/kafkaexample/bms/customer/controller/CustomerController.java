package com.kafkaexample.bms.customer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kafkaexample.bms.customer.model.Customer;
import com.kafkaexample.bms.customer.service.CustomerServiceDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class CustomerController {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	CustomerServiceDao customerService;

	@GetMapping("/email")
	public ResponseEntity<String> getEmail(@RequestParam Long accountNumber) {
		logger.info("Start");
		String email = customerService.getEmail(accountNumber);
		if (email == null || email.length() == 0) {
			logger.info("End - Email not found");
			return new ResponseEntity<>("Email Not Found", HttpStatus.BAD_REQUEST);
		} else {
			logger.info("End - Success");
			return new ResponseEntity<>(email, HttpStatus.OK);
		}
	}

	@PostMapping("/register")
	public ResponseEntity<Boolean> register(@RequestBody Customer customer) {
		logger.info("Start");
		logger.info("End - Success");
		return new ResponseEntity<>(customerService.register(customer), HttpStatus.OK);
	}

	@PostMapping("/update")
	public ResponseEntity<Boolean> update(@RequestHeader("Authorization") String token,
			@RequestBody Customer customer) {
		logger.info("Start");
		if (!customerService.validateToken(token)) {
			logger.info("End - Unauthorized");
			return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
		}
		logger.info("End - Success");
		return new ResponseEntity<>(customerService.update(customer), HttpStatus.OK);
	}

	@GetMapping("/viewDetails")
	public ResponseEntity<Customer> viewDetails(@RequestHeader("Authorization") String token,
			@RequestParam Long customerId) {
		logger.info("Start");
		if (!customerService.validateToken(token)) {
			logger.info("End - Unauthorized");
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}
		logger.info("End - Success");
		return new ResponseEntity<>(customerService.viewDetails(customerId), HttpStatus.OK);
	}

}
