package com.kafkaexample.bms.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kafkaexample.bms.customer.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

	@Query(value = "SELECT email FROM Customer c WHERE c.accountNumber = :accountNumber")
	public String getEmailByAccountNumber(@Param("accountNumber") Long accountNumber);
}
