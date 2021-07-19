package com.kafkaexample.bms.customer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.kafkaexample.bms.customer.config.CustomerMessageProducer;
import com.kafkaexample.bms.customer.config.UserCredentialsProducer;
import com.kafkaexample.bms.customer.model.Customer;
import com.kafkaexample.bms.customer.model.UserCredentials;
import com.kafkaexample.bms.customer.repository.CustomerRepository;
import com.kafkaexample.bms.customer.restclient.AuthorizationClient;

import jdk.internal.org.jline.utils.Log;

@Component
public class CustomerServiceDaoImpl implements CustomerServiceDao {
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	AuthorizationClient authorizationClient;
	@Autowired
	private CustomerMessageProducer customerMessageProducer;
	@Autowired
	private UserCredentialsProducer userCredentialsProducer;
	@Value("${spring.kafka.topic.registerCustomer}")
	private String REGISTER_CUSTOMER_TOPIC;
	@Value("${spring.kafka.topic.registerCustomerMessage}")
	private String REGISTER_CUSTOMER_MESSAGE_TOPIC;
	@Value("${spring.kafka.topic.updateCustomer}")
	private String UPDATE_CUSTOMER_TOPIC;
	@Value("${spring.kafka.topic.updateCustomerMessage}")
	private String UPDATE_CUSTOMER_MESSAGE_TOPIC;
	@Value("${spring.kafka.topic.failedCustomerMessage}")
	private String FAILED_CUSTOMER_MESSAGE_TOPIC;
	@Value("${spring.kafka.topic.userCredentials}")
	private String USER_CREDENTIALS_TOPIC;

	public boolean registerOrUpdateCustomer(String topic, Customer customer) {
		Log.info("Start");
		String[] toSend = new String[2];
		toSend[0] = customer.getEmail();
		try {
			customerRepository.save(customer);
			UserCredentials userCredentials = new UserCredentials(customer.getUsername(), customer.getPassword(), null);
			userCredentialsProducer.send(USER_CREDENTIALS_TOPIC, userCredentials);
			if (topic.equals("REGISTER_CUSTOMER_TOPIC")) {
				toSend[1] = "Registered Successfully";
				customerMessageProducer.send(REGISTER_CUSTOMER_MESSAGE_TOPIC, toSend);
				Log.info("End - Register - Success");
				return true;

			} else if (topic.equals("UPDATE_CUSTOMER_TOPIC")) {
				toSend[1] = "Updated Successfully";
				customerMessageProducer.send(UPDATE_CUSTOMER_MESSAGE_TOPIC, toSend);
				Log.info("End - Update - Success");
				return true;
			}
		} catch (Exception e) {
			toSend[1] = "Process Failed. Please try again.";
			customerMessageProducer.send(FAILED_CUSTOMER_MESSAGE_TOPIC, toSend);
			Log.error("End - Exception");
			return false;
		}
		Log.info("End - Invalid topic - Failed");
		return false;
	}

	public String getEmail(Long accountNumber) {
		Log.info("Start");
		try {
			Log.info("End - Success");
			return customerRepository.getEmailByAccountNumber(accountNumber);

		} catch (Exception e) {
			Log.error("End - Exception");
			return null;
		}
	}

	public boolean register(Customer customer) {
		Log.info("Start");
		try {
			customerRepository.save(customer);
			userCredentialsProducer.send(USER_CREDENTIALS_TOPIC,
					new UserCredentials(customer.getUsername(), customer.getPassword(), null));
			Log.info("End - Success");
			return true;

		} catch (Exception e) {
			Log.error("End - Exception");
			return false;
		}
	}

	public boolean update(Customer customer) {
		Log.info("Start");
		try {
			customerRepository.save(customer);
			Log.info("End - Success");
			return true;

		} catch (Exception e) {
			Log.error("End - Exception");
			return false;
		}
	}

	public Customer viewDetails(Long customerId) {
		Log.info("Start");
		try {
			Log.info("End - Success");
			return customerRepository.findByCustomerId(customerId);

		} catch (Exception e) {
			Log.error("End - Exception");
			return null;
		}
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
