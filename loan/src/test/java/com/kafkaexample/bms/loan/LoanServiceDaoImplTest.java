package com.kafkaexample.bms.loan;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.kafkaexample.bms.loan.config.ApplyLoanMessageProducer;
import com.kafkaexample.bms.loan.config.RetrieveLoanProducer;
import com.kafkaexample.bms.loan.model.Loan;
import com.kafkaexample.bms.loan.repository.LoanRepository;
import com.kafkaexample.bms.loan.restclient.AuthorizationClient;
import com.kafkaexample.bms.loan.restclient.CustomerClient;
import com.kafkaexample.bms.loan.service.LoanServiceDaoImpl;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class LoanServiceDaoImplTest {

	@InjectMocks
	private LoanServiceDaoImpl loanServiceDaoImpl;

	@Mock
	private LoanRepository loanRepository;

	@Mock
	ApplyLoanMessageProducer applyLoanMessageProducer;

	@Mock
	RetrieveLoanProducer retrieveLoanProducer;

	@Mock
	AuthorizationClient authorizationClient;

	private CustomerClient customerClient = Mockito.mock(CustomerClient.class, Mockito.RETURNS_DEEP_STUBS);

	@Test
	public void testApplyLoanSuccess() {

		Loan loan = new Loan();
		Mockito.when(loanRepository.save(loan)).thenReturn(loan);
		Mockito.when(customerClient.getEmail(Mockito.anyLong()).getBody()).thenReturn("test@mail.com");
		Assert.assertTrue(loanServiceDaoImpl.applyLoan(loan));
	}

	@Test
	public void testApplyLoanFailure() {
		Mockito.when(loanRepository.save(Mockito.any())).thenThrow(new DataIntegrityViolationException(""));
		Mockito.when(customerClient.getEmail(Mockito.anyLong()).getBody()).thenReturn("test@mail.com");

		Assert.assertFalse(loanServiceDaoImpl.applyLoan(new Loan()));
	}

	@Test
	public void testRetrieveLoanSuccess() {

		List<Loan> loanList = new ArrayList<>();
		loanList.add(new Loan());
		Mockito.when(loanRepository.findByAccountNumber(Mockito.anyLong())).thenReturn(loanList);
		assertEquals(1, loanServiceDaoImpl.retrieveLoans(Mockito.anyLong()).size());

	}

	@Test
	public void testRetrieveLoanFailure() {
		RetrieveLoanProducer retrieveLoanProducer = Mockito.mock(RetrieveLoanProducer.class);

		Mockito.when(loanRepository.findByAccountNumber(Mockito.anyLong())).thenThrow(new RuntimeException());
		assertNull(loanServiceDaoImpl.retrieveLoans(Mockito.anyLong()));
	}

	@Test
	public void testValidateTokenSuccess() {

		Mockito.when(authorizationClient.validateToken(Mockito.anyString()))
				.thenReturn(new ResponseEntity<>(HttpStatus.OK));
		assertTrue(loanServiceDaoImpl.validateToken(Mockito.anyString()));
	}

	@Test
	public void testValidateTokenFailure() {

		Mockito.when(authorizationClient.validateToken(Mockito.anyString()))
				.thenReturn(new ResponseEntity<>(HttpStatus.FORBIDDEN));
		assertFalse(loanServiceDaoImpl.validateToken(Mockito.anyString()));
	}

	@Test
	public void testValidateTokenFailureNull() {

		assertFalse(loanServiceDaoImpl.validateToken(null));
	}

	@Test
	public void testValidateTokenFailureException() {

		Mockito.when(authorizationClient.validateToken(Mockito.anyString())).thenThrow(new RuntimeException());
		assertFalse(loanServiceDaoImpl.validateToken(Mockito.anyString()));
	}
}
