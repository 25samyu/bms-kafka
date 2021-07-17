package com.kafkaexample.bms.loan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kafkaexample.bms.loan.model.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, String> {
	public List<Loan> findByAccountNumber(Long accountNumber);
}
