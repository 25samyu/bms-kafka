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

@RestController
public class CustomerController {

	@Autowired
	CustomerServiceDao customerService;

	@GetMapping("/email")
	public ResponseEntity<String> getEmail(@RequestParam Long accountNumber) {
		String email = customerService.getEmail(accountNumber);
		if (email == null || email.length() == 0) {
			return new ResponseEntity<>("Email Not Found", HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(email, HttpStatus.OK);
		}
	}

	@PostMapping("/register")
	public ResponseEntity<Boolean> register(@RequestBody Customer customer) {
		return new ResponseEntity<>(customerService.register(customer), HttpStatus.OK);
	}

	@PostMapping("/update")
	public ResponseEntity<Boolean> update(@RequestHeader("Authorization") String token,
			@RequestBody Customer customer) {
		if (!customerService.validateToken(token))
			return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
		return new ResponseEntity<>(customerService.update(customer), HttpStatus.OK);
	}

	@GetMapping("/viewDetails")
	public ResponseEntity<Customer> viewDetails(@RequestHeader("Authorization") String token,
			@RequestParam Long customerId) {
		if (!customerService.validateToken(token))
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		return new ResponseEntity<>(customerService.viewDetails(customerId), HttpStatus.OK);
	}

}
