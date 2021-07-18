package com.kafkaexample.bms.loan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kafkaexample.bms.loan.model.Loan;
import com.kafkaexample.bms.loan.service.LoanServiceDao;

import springfox.documentation.annotations.ApiIgnore;

@RestController
public class LoanController {
	@Autowired
	LoanServiceDao loanService;

	@ApiIgnore
	@GetMapping("/retrieveLoan")
	public void retrieveLoans(@RequestParam Long accountNumber) {
		loanService.retrieveLoans(accountNumber);
	}

	@PostMapping("/applyLoan")
	public ResponseEntity<Boolean> applyLoan(@RequestHeader("Authorization") String token, @RequestBody Loan loan) {
		if (!loanService.validateToken(token))
			return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
		return new ResponseEntity<>(loanService.applyLoan(loan), HttpStatus.OK);
	}

	@GetMapping("/viewLoan")
	public ResponseEntity<List<Loan>> viewLoans(@RequestHeader("Authorization") String token,
			@RequestParam Long accountNumber) {
		if (!loanService.validateToken(token))
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		return new ResponseEntity<>(loanService.retrieveLoans(accountNumber), HttpStatus.OK);
	}

}
