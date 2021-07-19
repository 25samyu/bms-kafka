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

import jdk.internal.org.jline.utils.Log;

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
		Log.info("Start");
		Log.info("End - Success");
		customerProducer.send(topic, customer);

	}

	public void applyLoan(String topic, Loan loan) {
		Log.info("Start");
		Log.info("End - Success");
		applyLoanProducer.send(topic, loan);
	}

	public void retrieveLoans(Long accountNumber) {
		Log.info("Start");
		Log.info("End - Success");
		loanClient.retrieveLoans(accountNumber);
	}

	public boolean login(UserCredentials userCredentials, HttpSession session, ModelMap warning) {
		Log.info("Start");
		try {
			ResponseEntity<String> response = authorizationClient.login(userCredentials);
			session.setAttribute("TOKEN", "Bearer " + response.getBody());
			// System.out.println("Bearer " + response.getBody());
			Log.info("End - Success");
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			if (e.getClass().toString().contains("feign.RetryableException")) {
				warning.addAttribute("loginMessage", "Server is down. Please try later.");
				Log.error("End - Exception");

			} else {
				warning.addAttribute("loginMessage", "Unable to login. Please check your credentials.");
				Log.info("End - Unauthorized");

			}
			Log.info("End - Unauthorized");
			return false;

		}
	}

	public void getMessage(String[] message) {
		Log.info("Start");
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setFrom("kafkatrial25@gmail.com");
			mailMessage.setTo(message[0]);
			mailMessage.setSubject("Bank Management System");
			mailMessage.setText(message[1]);
			emailSender.send(mailMessage);
			Log.info("End - Success");
		} catch (Exception e) {
			Log.info("End - Failed to send email");
		}
	}

	public void saveLoans(List<Loan> loanList) {
		Log.info("Start");
		Log.info("End - Success");
		globalLoanList = loanList;
	}

	public List<Loan> returnLoans() {
		Log.info("Start");
		LocalTime startTime = LocalTime.now();
		while ((globalLoanList == null || globalLoanList.size() == 0)
				&& startTime.until(LocalTime.now(), ChronoUnit.SECONDS) < 3)
			;
		List<Loan> loanList = globalLoanList;
		globalLoanList = null;
		Log.info("End - Success");
		return loanList;
	}

	public boolean validateToken(String token) {
		Log.info("Start");
		try {
			if (token == null) {
				Log.info("End - Unauthorized");
				return false;
			}
			HttpStatus loginStatusCode = authorizationClient.validateToken(token).getStatusCode();
			if (loginStatusCode.equals(HttpStatus.OK)) {
				Log.info("End - Success");
				return true;
			} else {
				Log.info("End - Unauthorized");
				return false;
			}
		} catch (Exception e) {
			Log.error("End - Exception");
			return false;
		}
	}

}
