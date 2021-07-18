package com.kafkaexample.bms.loan.service;

import java.util.List;

import com.kafkaexample.bms.loan.model.Loan;

public interface LoanServiceDao {
	public boolean applyLoan(Loan loan);

	public List<Loan> retrieveLoans(Long accountNumber);

	public boolean validateToken(String token);
}
