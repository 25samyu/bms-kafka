package com.kafkaexample.bms.loan;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkaexample.bms.loan.controller.LoanController;
import com.kafkaexample.bms.loan.model.Loan;
import com.kafkaexample.bms.loan.service.LoanServiceDao;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
public class LoanServiceControllerTest {
	@InjectMocks
	private LoanController loanController;
	@Autowired
	private MockMvc mockMvc;
	@Mock
	LoanServiceDao loanService;

	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);

		this.mockMvc = MockMvcBuilders.standaloneSetup(loanController).build();

	}

	@Test
	public void testRetrieveLoan() throws Exception {
		Mockito.when(loanService.retrieveLoans(Mockito.anyLong())).thenReturn(new ArrayList<>());
		ResultActions actions = mockMvc.perform(get("/retrieveLoan?accountNumber=123"));
		actions.andExpect(status().isOk());

	}

	@Test
	public void testViewLoansSuccess() throws Exception {
		Mockito.when(loanService.retrieveLoans(Mockito.anyLong())).thenReturn(new ArrayList<>());
		Mockito.when(loanService.validateToken(Mockito.anyString())).thenReturn(true);
		ResultActions actions = mockMvc
				.perform(get("/viewLoan?accountNumber=123").header("Authorization", Mockito.anyString()));
		actions.andExpect(status().isOk());

	}

	@Test
	public void testViewLoansFailure() throws Exception {
		Mockito.when(loanService.retrieveLoans(Mockito.anyLong())).thenReturn(new ArrayList<>());
		Mockito.when(loanService.validateToken(Mockito.anyString())).thenReturn(false);
		ResultActions actions = mockMvc
				.perform(get("/viewLoan?accountNumber=123").header("Authorization", Mockito.anyString()));
		actions.andExpect(status().isForbidden());

	}

	@Test
	public void testApplyLoanSuccess() throws Exception {
		Loan loan = new Loan();
		Mockito.when(loanService.applyLoan(loan)).thenReturn(true);
		Mockito.when(loanService.validateToken(Mockito.anyString())).thenReturn(true);
		ResultActions actions = mockMvc.perform(post("/applyLoan").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(loan)).header("Authorization", Mockito.anyString()));
		actions.andExpect(status().isOk());

	}

	@Test
	public void testApplyLoanFailure() throws Exception {
		Loan loan = new Loan();
		Mockito.when(loanService.applyLoan(loan)).thenReturn(true);
		Mockito.when(loanService.validateToken(Mockito.anyString())).thenReturn(false);
		ResultActions actions = mockMvc.perform(post("/applyLoan").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(loan)).header("Authorization", Mockito.anyString()));
		actions.andExpect(status().isForbidden());

	}

	public static String asJsonString(Loan loan) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(loan);

	}

}
