package com.kafkaexample.bms.portal.service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import com.kafkaexample.bms.portal.config.ApplyLoanProducer;
import com.kafkaexample.bms.portal.config.CustomerProducer;
import com.kafkaexample.bms.portal.model.Customer;
import com.kafkaexample.bms.portal.model.Loan;
import com.kafkaexample.bms.portal.model.UserCredentials;
import com.kafkaexample.bms.portal.restclient.AuthorizationClient;
import com.kafkaexample.bms.portal.restclient.LoanClient;

@Component
public class BmsPortalServiceDaoImpl implements BmsPortalServiceDao {
	@Autowired
	private CustomerProducer customerProducer;

	@Autowired
	private ApplyLoanProducer applyLoanProducer;

	@Autowired
	AuthorizationClient authorizationClient;

	@Autowired
	LoanClient loanClient;

	List<Loan> globalLoanList;
	String globalLoginMessage;
	String globalValidateMessage;
	@Autowired
	private JavaMailSender emailSender;

	public void registerOrUpdateCustomer(String topic, Customer customer) {
		customerProducer.send(topic, customer);

	}

	public void applyLoan(String topic, Loan loan) {
		applyLoanProducer.send(topic, loan);
	}

	public void retrieveLoans(Long accountNumber) {
		loanClient.retrieveLoans(accountNumber);
	}

	public boolean login(UserCredentials userCredentials, HttpSession session, ModelMap warning) {
		try {
			ResponseEntity<String> response = authorizationClient.login(userCredentials);
			session.setAttribute("TOKEN", "Bearer " + response.getBody());
			System.out.println("Bearer " + response.getBody());
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			if (e.getClass().toString().contains("feign.RetryableException"))
				warning.addAttribute("loginMessage", "Server is down. Please try later.");

			else
				warning.addAttribute("loginMessage", "Unable to login. Please check your credentials.");

			return false;

		}
	}

	public void getMessage(String[] message) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("kafkatrial25@gmail.com");
		mailMessage.setTo(message[0]);
		mailMessage.setSubject("Bank Management System");
		mailMessage.setText(message[1]);
		emailSender.send(mailMessage);
	}

	public void saveLoans(List<Loan> loanList) {
		globalLoanList = loanList;
	}

	public List<Loan> returnLoans() {
		LocalTime startTime = LocalTime.now();
		while ((globalLoanList == null || globalLoanList.size() == 0)
				&& startTime.until(LocalTime.now(), ChronoUnit.SECONDS) < 3)
			;
		List<Loan> loanList = globalLoanList;
		globalLoanList = null;
		return loanList;
	}

	public boolean validateToken(String token) {
		try {
			if (token == null)
				return false;
			HttpStatus loginStatusCode = authorizationClient.validateToken(token).getStatusCode();
			if (loginStatusCode.equals(HttpStatus.OK)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

}
