package com.kafkaexample.bms.portal.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import com.kafkaexample.bms.portal.model.Customer;
import com.kafkaexample.bms.portal.model.Loan;
import com.kafkaexample.bms.portal.model.UserCredentials;

@Component
public interface BmsPortalServiceDao {
	public void registerOrUpdateCustomer(String topic, Customer customer);

	public void applyLoan(String topic, Loan loan);

	public void retrieveLoans(Long accountNumber);

	public boolean login(UserCredentials userCredentials, HttpSession session, ModelMap warning);

	public void getMessage(String[] message);

	public void saveLoans(List<Loan> loanList);

	public List<Loan> returnLoans();

	public boolean validateToken(String token);

}
