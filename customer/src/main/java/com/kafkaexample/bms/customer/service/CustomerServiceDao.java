package com.kafkaexample.bms.customer.service;

import com.kafkaexample.bms.customer.model.Customer;

public interface CustomerServiceDao {
	public boolean registerOrUpdateCustomer(String topic, Customer customer);

	public String getEmail(Long accountNumber);


}
