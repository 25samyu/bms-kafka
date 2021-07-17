package com.kafkaexample.bms.loan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kafkaexample.bms.loan.service.LoanServiceDao;

@RestController
public class LoanController {
	@Autowired
	LoanServiceDao loanService;

	@GetMapping("/retrieveLoan")
	public void retrieveLoans(@RequestParam Long accountNumber) {
		loanService.retrieveLoans(accountNumber);
	}

}
