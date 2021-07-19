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

import jdk.internal.org.jline.utils.Log;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
public class LoanController {
	@Autowired
	LoanServiceDao loanService;

	@ApiIgnore
	@GetMapping("/retrieveLoan")
	public void retrieveLoans(@RequestParam Long accountNumber) {
		Log.info("Start");
		Log.info("End - Success");
		loanService.retrieveLoans(accountNumber);
	}

	@PostMapping("/applyLoan")
	public ResponseEntity<Boolean> applyLoan(@RequestHeader("Authorization") String token, @RequestBody Loan loan) {
		Log.info("Start");
		if (!loanService.validateToken(token)) {
			Log.info("End - Unauthorized");
			return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
		}
		Log.info("End - Success");
		return new ResponseEntity<>(loanService.applyLoan(loan), HttpStatus.OK);
	}

	@GetMapping("/viewLoan")
	public ResponseEntity<List<Loan>> viewLoans(@RequestHeader("Authorization") String token,
			@RequestParam Long accountNumber) {
		Log.info("Start");
		if (!loanService.validateToken(token)) {
			Log.info("End - Unauthorized");
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}
		Log.info("End - Success");
		return new ResponseEntity<>(loanService.retrieveLoans(accountNumber), HttpStatus.OK);
	}

}
