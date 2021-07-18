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
		String[] toSend = new String[2];
		toSend[0] = customer.getEmail();
		try {
			customerRepository.save(customer);
			UserCredentials userCredentials = new UserCredentials(customer.getUsername(), customer.getPassword(), null);
			userCredentialsProducer.send(USER_CREDENTIALS_TOPIC, userCredentials);
			if (topic.equals(REGISTER_CUSTOMER_TOPIC)) {
				toSend[1] = "Registered Successfully";
				customerMessageProducer.send(REGISTER_CUSTOMER_MESSAGE_TOPIC, toSend);
				return true;

			} else if (topic.equals(UPDATE_CUSTOMER_TOPIC)) {
				toSend[1] = "Updated Successfully";
				customerMessageProducer.send(UPDATE_CUSTOMER_MESSAGE_TOPIC, toSend);

				return true;
			}
		} catch (Exception e) {
			toSend[1] = "Process Failed. Please try again.";
			customerMessageProducer.send(FAILED_CUSTOMER_MESSAGE_TOPIC, toSend);

			return false;
		}
		return false;
	}

	public String getEmail(Long accountNumber) {
		try {
			return customerRepository.getEmailByAccountNumber(accountNumber);

		} catch (Exception e) {
			return null;
		}
	}

	public boolean register(Customer customer) {
		try {
			customerRepository.save(customer);
			userCredentialsProducer.send(USER_CREDENTIALS_TOPIC,
					new UserCredentials(customer.getUsername(), customer.getPassword(), null));
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	public boolean update(Customer customer) {
		try {
			customerRepository.save(customer);
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	public Customer viewDetails(Long customerId) {
		try {
			return customerRepository.findByCustomerId(customerId);

		} catch (Exception e) {
			return null;
		}
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
