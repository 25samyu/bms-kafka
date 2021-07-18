package com.kafkaexample.bms.customer.service;

import com.kafkaexample.bms.customer.model.Customer;

public interface CustomerServiceDao {
	public boolean registerOrUpdateCustomer(String topic, Customer customer);

	public String getEmail(Long accountNumber);

	public boolean register(Customer customer);

	public boolean update(Customer customer);

	public Customer viewDetails(Long customerId);

	public boolean validateToken(String token);

}
