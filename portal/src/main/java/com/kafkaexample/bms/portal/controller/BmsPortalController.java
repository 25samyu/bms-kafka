package com.kafkaexample.bms.portal.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/portal")
public class BmsPortalController {
	
	private static final Logger logger = LoggerFactory.getLogger(BmsPortalController.class);


	@Autowired
	BmsPortalServiceDao bmsPortalService;

	@Value("${spring.kafka.topic.registerCustomer}")
	String REGISTER_CUSTOMER_TOPIC;

	@Value("${spring.kafka.topic.updateCustomer}")
	String UPDATE_CUSTOMER_TOPIC;

	@Value("${spring.kafka.topic.applyLoan}")
	String APPLY_LOAN_TOPIC;

	@PostMapping("/register")
	public String register(@RequestBody Customer customer, ModelMap model) {
		logger.info("Start");
		bmsPortalService.registerOrUpdateCustomer(REGISTER_CUSTOMER_TOPIC, customer);
		model.addAttribute("registrationMessage",
				"Registration process initiated. Please check your email to confirm your registration status.");
		logger.info("End - Success");
		return "after-registration";
	}

	@PostMapping("/update")
	public String update(@RequestBody Customer customer, HttpSession session, ModelMap model) {
		logger.info("Start");
		if ((session == null) || (!bmsPortalService.validateToken((String) session.getAttribute("TOKEN")))) {
			logger.info("End - Unauthorized");
			return "redirect:/portal/logout";
		}
		try {
			bmsPortalService.registerOrUpdateCustomer(UPDATE_CUSTOMER_TOPIC, customer);
			model.addAttribute("updateMessage", "Please check your email to verify your updation status");
			logger.info("End - Success");
			return "after-update";
		} catch (Exception e) {
			logger.error("End - Exception");
			return "error";
		}
	}

	@PostMapping("/apply")
	public String apply(@RequestBody Loan loan, HttpSession session, ModelMap model) {
		logger.info("Start");
		if ((session == null) || (!bmsPortalService.validateToken((String) session.getAttribute("TOKEN")))) {
			logger.info("End - Unauthorized");
			return "redirect:/portal/logout";
		}
		try {
			bmsPortalService.applyLoan(APPLY_LOAN_TOPIC, loan);
			model.addAttribute("applyLoanMessage", "Please check your email to verify your loan application status");
			logger.info("End - Success");
			return "after-loan-application";
		} catch (Exception e) {
			logger.info("End - Exception");
			return "error";
		}
	}

	@GetMapping("/retrieve")
	public String retrieve(@RequestParam Long accountNumber, HttpSession session, ModelMap model) {
		logger.info("Start");
		if ((session == null) || (!bmsPortalService.validateToken((String) session.getAttribute("TOKEN")))) {
			logger.info("End - Unauthorized");
			return "redirect:/portal/logout";
		}

		bmsPortalService.retrieveLoans(accountNumber);
		model.addAttribute("loanHistory", bmsPortalService.returnLoans());
		logger.info("End - Success");
		return "loan-history";
	}

	@PostMapping("/login")
	public String login(@RequestBody UserCredentials userCredentials, HttpSession session, ModelMap warning) {
		logger.info("End - Start");
		if (bmsPortalService.login(userCredentials, session, warning)) {
			logger.info("End - Success");
			return "home";
		} else {
			logger.info("End - Unauthorized");
			return "login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		logger.info("Start");
		session.invalidate();
		logger.info("End - Success");
		return "login";
	}

	@GetMapping("/getLogin")
	public String getLoginPage(HttpSession session) {
		logger.info("Start");
		logger.info("End - Success");
		return "login";
	}

	@GetMapping("/getRegistrationForm")
	public String getRegistrationForm() {
		logger.info("Start");
		logger.info("End - Success");
		return "registration";
	}

	@GetMapping("/getUpdateForm")
	public String getUpdateForm(HttpSession session) {
		logger.info("Start");
		if ((session == null) || (!bmsPortalService.validateToken((String) session.getAttribute("TOKEN")))) {
			logger.info("End - Unauthorized");
			return "redirect:/portal/logout";
		}
		logger.info("End - Success");
		return "update";
	}

	@GetMapping("/getLoanApplication")
	public String getLoanApplication(HttpSession session) {
		logger.info("Start");
		if ((session == null) || (!bmsPortalService.validateToken((String) session.getAttribute("TOKEN")))) {
			logger.info("End - Unauthorized");
			return "redirect:/portal/logout";
		}
		logger.info("End - Success");
		return "loan-application";
	}

	@GetMapping("/home")
	public String getHome(HttpSession session) {
		logger.info("Start");
		if ((session == null) || (!bmsPortalService.validateToken((String) session.getAttribute("TOKEN")))) {
			logger.info("End - Unauthorized");
			return "redirect:/portal/logout";
		}
		logger.info("End - Success");
		return "home";
	}
}
