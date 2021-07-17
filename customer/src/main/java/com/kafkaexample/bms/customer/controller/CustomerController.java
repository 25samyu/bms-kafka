package com.kafkaexample.bms.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
