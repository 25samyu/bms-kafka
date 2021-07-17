package com.kafkaexample.bms.loan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kafkaexample.bms.loan.config.ApplyLoanMessageProducer;
import com.kafkaexample.bms.loan.config.RetrieveLoanProducer;
import com.kafkaexample.bms.loan.model.Loan;
import com.kafkaexample.bms.loan.repository.LoanRepository;
import com.kafkaexample.bms.loan.restclient.CustomerClient;

@Component
public class LoanServiceDaoImpl implements LoanServiceDao {
	@Autowired
	LoanRepository loanRepository;
	@Autowired
	private ApplyLoanMessageProducer applyLoanMessageProducer;

	@Autowired
	private RetrieveLoanProducer retrieveLoanProducer;

	@Autowired
	CustomerClient customerClient;
	@Value("${spring.kafka.topic.applyLoanMessage}")
	String APPLY_LOAN_MESSAGE_TOPIC;

	@Value("${spring.kafka.topic.failedLoanMessage}")
	String FAILED_LOAN_MESSAGE_TOPIC;

	@Value("${spring.kafka.topic.retrieveLoan}")
	String RETRIEVE_LOAN_TOPIC;

	@Value("${spring.kafka.topic.failedRetrieveLoan}")
	String FAILED_RETRIEVE_LOAN_TOPIC;

	public boolean applyLoan(Loan loan) {
		String[] toSend = new String[2];
		toSend[0] = customerClient.getEmail(loan.getAccountNumber()).getBody();
		try {
			loanRepository.save(loan);
			toSend[1] = "Loan Applied Successfully";
			applyLoanMessageProducer.send(APPLY_LOAN_MESSAGE_TOPIC, toSend);

			return true;

		} catch (Exception e) {
			toSend[1] = "Process Failed. Please try again.";
			applyLoanMessageProducer.send(FAILED_LOAN_MESSAGE_TOPIC, toSend);

			return false;
		}
	}

	public void retrieveLoans(Long accountNumber) {
		try {
			List<Loan> loanList = loanRepository.findByAccountNumber(accountNumber);
			retrieveLoanProducer.send(RETRIEVE_LOAN_TOPIC, loanList);
		} catch (Exception e) {
			retrieveLoanProducer.send(FAILED_RETRIEVE_LOAN_TOPIC, null);
		}
	}
}
