package com.kafkaexample.bms.loan.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.kafkaexample.bms.loan.config.ApplyLoanMessageProducer;
import com.kafkaexample.bms.loan.config.RetrieveLoanProducer;
import com.kafkaexample.bms.loan.model.Loan;
import com.kafkaexample.bms.loan.repository.LoanRepository;
import com.kafkaexample.bms.loan.restclient.AuthorizationClient;
import com.kafkaexample.bms.loan.restclient.CustomerClient;

@Component
public class LoanServiceDaoImpl implements LoanServiceDao {

	private static final Logger logger = LoggerFactory.getLogger(LoanServiceDaoImpl.class);

	@Autowired
	LoanRepository loanRepository;
	@Autowired
	AuthorizationClient authorizationClient;

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
		logger.info("Start");
		String[] toSend = new String[2];
		toSend[0] = customerClient.getEmail(loan.getAccountNumber()).getBody();
		try {
			loanRepository.save(loan);
			toSend[1] = "Loan Applied Successfully";
			applyLoanMessageProducer.send(APPLY_LOAN_MESSAGE_TOPIC, toSend);
			logger.info("End - Success");
			return true;

		} catch (Exception e) {
			toSend[1] = "Process Failed. Please try again.";
			applyLoanMessageProducer.send(FAILED_LOAN_MESSAGE_TOPIC, toSend);
			logger.error("End - Exception");
			return false;
		}
	}

	public List<Loan> retrieveLoans(Long accountNumber) {
		logger.info("Start");
		try {
			List<Loan> loanList = loanRepository.findByAccountNumber(accountNumber);
			retrieveLoanProducer.send(RETRIEVE_LOAN_TOPIC, loanList);
			logger.info("End - Success");
			return loanList;
		} catch (Exception e) {
			retrieveLoanProducer.send(FAILED_RETRIEVE_LOAN_TOPIC, null);
			logger.error("End - Exception");
			return null;
		}
	}

	public boolean validateToken(String token) {
		logger.info("Start");
		try {
			if (token == null) {
				logger.info("End - Unauthorized");
				return false;
			}
			HttpStatus loginStatusCode = authorizationClient.validateToken(token).getStatusCode();
			if (loginStatusCode.equals(HttpStatus.OK)) {
				logger.info("End - Success");
				return true;
			} else {
				logger.info("End - Unauthorized");
				return false;
			}
		} catch (Exception e) {
			logger.error("End - Exception");
			return false;
		}
	}
}
