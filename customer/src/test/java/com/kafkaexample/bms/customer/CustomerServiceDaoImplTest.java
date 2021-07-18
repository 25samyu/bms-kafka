package com.kafkaexample.bms.customer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.kafkaexample.bms.customer.config.CustomerMessageProducer;
import com.kafkaexample.bms.customer.config.UserCredentialsProducer;
import com.kafkaexample.bms.customer.model.Customer;
import com.kafkaexample.bms.customer.repository.CustomerRepository;
import com.kafkaexample.bms.customer.restclient.AuthorizationClient;
import com.kafkaexample.bms.customer.service.CustomerServiceDaoImpl;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceDaoImplTest {
	@InjectMocks
	private CustomerServiceDaoImpl customerServiceDaoImpl;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	CustomerMessageProducer customerMessageProducer;

	@Mock
	UserCredentialsProducer userCredentialsProducer;

	@Mock
	AuthorizationClient authorizationClient;

	@Test
	public void testregisterOrUpdateCustomerRegisterSuccess() {

		Customer customer = new Customer();
		Mockito.when(customerRepository.save(customer)).thenReturn(customer);
		Assert.assertTrue(customerServiceDaoImpl.registerOrUpdateCustomer("REGISTER_CUSTOMER_TOPIC", customer));

	}

	@Test
	public void testregisterOrUpdateCustomerUpdateSuccess() {

		Customer customer = new Customer();
		Mockito.when(customerRepository.save(customer)).thenReturn(customer);
		Assert.assertTrue(customerServiceDaoImpl.registerOrUpdateCustomer("UPDATE_CUSTOMER_TOPIC", customer));

	}

	@Test
	public void testregisterOrUpdateCustomerFailure() {

		Customer customer = new Customer();
		Mockito.when(customerRepository.save(customer)).thenReturn(customer);
		Assert.assertFalse(customerServiceDaoImpl.registerOrUpdateCustomer("TEST_TOPIC", customer));

	}

	@Test
	public void testregisterOrUpdateCustomerExceptionFailure() {

		Customer customer = new Customer();
		Mockito.when(customerRepository.save(customer)).thenThrow(new RuntimeException());
		Assert.assertFalse(customerServiceDaoImpl.registerOrUpdateCustomer("REGISTER_CUSTOMER_TOPIC", customer));

	}

	@Test
	public void testGetEmailSuccess() {

		Mockito.when(customerRepository.getEmailByAccountNumber(Mockito.anyLong())).thenReturn("test@mail.com");
		Assert.assertEquals("test@mail.com", customerServiceDaoImpl.getEmail(Mockito.anyLong()));

	}

	@Test
	public void testGetEmailExceptionFailure() {

		Mockito.when(customerRepository.getEmailByAccountNumber(Mockito.anyLong())).thenThrow(new RuntimeException());
		Assert.assertNull(customerServiceDaoImpl.getEmail(Mockito.anyLong()));

	}

	@Test
	public void testRegisterCustomerSuccess() {

		Customer customer = new Customer();
		Mockito.when(customerRepository.save(customer)).thenReturn(customer);
		Assert.assertTrue(customerServiceDaoImpl.register(customer));

	}

	@Test
	public void testRegisterCustomerExceptionFailure() {

		Customer customer = new Customer();
		Mockito.when(customerRepository.save(customer)).thenThrow(new RuntimeException());
		Assert.assertFalse(customerServiceDaoImpl.register(customer));

	}

	@Test
	public void testUpdateCustomerSuccess() {

		Customer customer = new Customer();
		Mockito.when(customerRepository.save(customer)).thenReturn(customer);
		Assert.assertTrue(customerServiceDaoImpl.update(customer));

	}

	@Test
	public void testUpdateCustomerExceptionFailure() {

		Customer customer = new Customer();
		Mockito.when(customerRepository.save(customer)).thenThrow(new RuntimeException());
		Assert.assertFalse(customerServiceDaoImpl.update(customer));

	}

	@Test
	public void testViewDetailsSuccess() {

		Customer customer = new Customer();
		Mockito.when(customerRepository.findByCustomerId(Mockito.anyLong())).thenReturn(customer);
		Assert.assertEquals(customer, customerServiceDaoImpl.viewDetails(Mockito.anyLong()));

	}

	@Test
	public void testViewDetailsExceptionFailure() {

		Mockito.when(customerRepository.findByCustomerId(Mockito.anyLong())).thenThrow(new RuntimeException());
		Assert.assertNull(customerServiceDaoImpl.viewDetails(Mockito.anyLong()));

	}

	@Test
	public void testValidateTokenSuccess() {

		Mockito.when(authorizationClient.validateToken(Mockito.anyString()))
				.thenReturn(new ResponseEntity<>(HttpStatus.OK));
		assertTrue(customerServiceDaoImpl.validateToken(Mockito.anyString()));
	}

	@Test
	public void testValidateTokenFailure() {

		Mockito.when(authorizationClient.validateToken(Mockito.anyString()))
				.thenReturn(new ResponseEntity<>(HttpStatus.FORBIDDEN));
		assertFalse(customerServiceDaoImpl.validateToken(Mockito.anyString()));
	}

	@Test
	public void testValidateTokenFailureNull() {

		assertFalse(customerServiceDaoImpl.validateToken(null));
	}

	@Test
	public void testValidateTokenFailureException() {

		Mockito.when(authorizationClient.validateToken(Mockito.anyString())).thenThrow(new RuntimeException());
		assertFalse(customerServiceDaoImpl.validateToken(Mockito.anyString()));
	}
}
