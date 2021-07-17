package com.kafkaexample.bms.loan.service;

import com.kafkaexample.bms.loan.model.Loan;

public interface LoanServiceDao {
	public boolean applyLoan(Loan loan);

	public void retrieveLoans(Long accountNumber);
}
