package com.kafkaexample.bms.portal.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kafkaexample.bms.portal.model.Customer;
import com.kafkaexample.bms.portal.model.Loan;
import com.kafkaexample.bms.portal.model.UserCredentials;
import com.kafkaexample.bms.portal.service.BmsPortalServiceDao;

@Controller
@RequestMapping("/portal")
public class BmsPortalController {
	@Autowired
	BmsPortalServiceDao bmsPortalService;

	@Value("${spring.kafka.topic.registerCustomer}")
	String REGISTER_CUSTOMER_TOPIC;

	@Value("${spring.kafka.topic.updateCustomer}")
	String UPDATE_CUSTOMER_TOPIC;

	@Value("${spring.kafka.topic.applyLoan}")
	String APPLY_LOAN_TOPIC;

	@Value("${spring.kafka.topic.login}")
	String LOGIN_TOPIC;

	@Value("${spring.kafka.topic.validateToken}")
	String VALIDATE_TOKEN_TOPIC;

	@PostMapping("/register")
	public String register(@RequestBody Customer customer, ModelMap model) {

		bmsPortalService.registerOrUpdateCustomer(REGISTER_CUSTOMER_TOPIC, customer);
		model.addAttribute("registrationMessage",
				"Registration process initiated. Please check your email to confirm your registration status.");
		return "after-registration";
	}

	@PostMapping("/update")
	public String update(@RequestBody Customer customer, HttpSession session, ModelMap model) {
		if ((session == null) || (!bmsPortalService.validateToken((String) session.getAttribute("TOKEN"))))
			return "redirect:/portal/logout";
		try {
			bmsPortalService.registerOrUpdateCustomer(UPDATE_CUSTOMER_TOPIC, customer);
			model.addAttribute("updateMessage", "Please check your email to verify your updation status");
			return "after-update";
		} catch (Exception e) {
			return "error";
		}
	}

	@PostMapping("/apply")
	public String apply(@RequestBody Loan loan, HttpSession session, ModelMap model) {
		if ((session == null) || (!bmsPortalService.validateToken((String) session.getAttribute("TOKEN"))))
			return "redirect:/portal/logout";
		try {
			bmsPortalService.applyLoan(APPLY_LOAN_TOPIC, loan);
			model.addAttribute("applyLoanMessage", "Please check your email to verify your loan application status");
			return "after-loan-application";
		} catch (Exception e) {
			return "error";
		}
	}

	@GetMapping("/retrieve")
	public String retrieve(@RequestParam Long accountNumber, HttpSession session, ModelMap model) {
		if ((session == null) || (!bmsPortalService.validateToken((String) session.getAttribute("TOKEN"))))
			return "redirect:/portal/logout";

		bmsPortalService.retrieveLoans(accountNumber);
		model.addAttribute("loanHistory", bmsPortalService.returnLoans());
		return "loan-history";
	}

	@GetMapping("/displayLoans")
	public List<Loan> displayLoans() {
		return bmsPortalService.returnLoans();

	}

	@PostMapping("/login")
	public String login(@RequestBody UserCredentials userCredentials, HttpSession session, ModelMap warning) {
		if (bmsPortalService.login(userCredentials, session, warning))
			return "home";
		else
			return "login";

	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "login";
	}

	@GetMapping("/getLogin")
	public String getLoginPage(HttpSession session) {
		return "login";
	}

	@GetMapping("/getRegistrationForm")
	public String getRegistrationForm() {
		return "registration";
	}

	@GetMapping("/getUpdateForm")
	public String getUpdateForm(HttpSession session) {
		if ((session == null) || (!bmsPortalService.validateToken((String) session.getAttribute("TOKEN"))))
			return "redirect:/portal/logout";
		return "update";
	}

	@GetMapping("/getLoanApplication")
	public String getLoanApplication(HttpSession session) {
		if ((session == null) || (!bmsPortalService.validateToken((String) session.getAttribute("TOKEN"))))
			return "redirect:/portal/logout";
		return "loan-application";
	}

	@GetMapping("/home")
	public String getHome(HttpSession session) {
		System.out.println(session.getAttribute("TOKEN"));
		if ((session == null) || (!bmsPortalService.validateToken((String) session.getAttribute("TOKEN"))))
			return "redirect:/portal/logout";
		return "home";
	}
}
