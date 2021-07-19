package com.kafkaexample.bms.customer.controller;

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

import jdk.internal.org.jline.utils.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class CustomerController {

	@Autowired
	CustomerServiceDao customerService;

	@GetMapping("/email")
	public ResponseEntity<String> getEmail(@RequestParam Long accountNumber) {
		Log.info("Start");
		String email = customerService.getEmail(accountNumber);
		if (email == null || email.length() == 0) {
			Log.info("End - Email not found");
			return new ResponseEntity<>("Email Not Found", HttpStatus.BAD_REQUEST);
		} else {
			Log.info("End - Success");
			return new ResponseEntity<>(email, HttpStatus.OK);
		}
	}

	@PostMapping("/register")
	public ResponseEntity<Boolean> register(@RequestBody Customer customer) {
		Log.info("Start");
		Log.info("End - Success");
		return new ResponseEntity<>(customerService.register(customer), HttpStatus.OK);
	}

	@PostMapping("/update")
	public ResponseEntity<Boolean> update(@RequestHeader("Authorization") String token,
			@RequestBody Customer customer) {
		Log.info("Start");
		if (!customerService.validateToken(token)) {
			Log.info("End - Unauthorized");
			return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
		}
		Log.info("End - Success");
		return new ResponseEntity<>(customerService.update(customer), HttpStatus.OK);
	}

	@GetMapping("/viewDetails")
	public ResponseEntity<Customer> viewDetails(@RequestHeader("Authorization") String token,
			@RequestParam Long customerId) {
		Log.info("Start");
		if (!customerService.validateToken(token)) {
			Log.info("End - Unauthorized");
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}
		Log.info("End - Success");
		return new ResponseEntity<>(customerService.viewDetails(customerId), HttpStatus.OK);
	}

}
