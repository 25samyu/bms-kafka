package com.kafkaexample.bms.customer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.kafkaexample.bms.customer.controller.CustomerController;
import com.kafkaexample.bms.customer.model.Customer;
import com.kafkaexample.bms.customer.service.CustomerServiceDao;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
public class CustomerServiceControllerTest {
	@InjectMocks
	private CustomerController customerController;
	@Autowired
	private MockMvc mockMvc;
	@Mock
	CustomerServiceDao customerService;

	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);

		this.mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

	}

	@Test
	public void testGetEmailSuccess() throws Exception {
		Mockito.when(customerService.getEmail(Mockito.anyLong())).thenReturn("test@mail.com");
		ResultActions actions = mockMvc.perform(get("/email?accountNumber=123"));
		actions.andExpect(status().isOk());

	}

	@Test
	public void testGetEmailNullFailure() throws Exception {
		Mockito.when(customerService.getEmail(Mockito.anyLong())).thenReturn(null);
		ResultActions actions = mockMvc.perform(get("/email?accountNumber=123"));
		actions.andExpect(status().isBadRequest());

	}

	@Test
	public void testGetEmailZeroLengthFailure() throws Exception {
		Mockito.when(customerService.getEmail(Mockito.anyLong())).thenReturn("");
		ResultActions actions = mockMvc.perform(get("/email?accountNumber=123"));
		actions.andExpect(status().isBadRequest());

	}

	@Test
	public void testRegister() throws Exception {
		Customer customer = new Customer();
		Mockito.when(customerService.register(customer)).thenReturn(true);
		ResultActions actions = mockMvc
				.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(asJsonString(customer)));
		actions.andExpect(status().isOk());
	}

	@Test
	public void testUpdateSuccess() throws Exception {
		Customer customer = new Customer();
		Mockito.when(customerService.validateToken(Mockito.anyString())).thenReturn(true);
		Mockito.when(customerService.update(customer)).thenReturn(true);
		ResultActions actions = mockMvc.perform(post("/update").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(customer)).header("Authorization", Mockito.anyString()));
		actions.andExpect(status().isOk());
	}

	@Test
	public void testUpdateFailure() throws Exception {
		Customer customer = new Customer();
		Mockito.when(customerService.validateToken(Mockito.anyString())).thenReturn(false);
		Mockito.when(customerService.update(customer)).thenReturn(true);
		ResultActions actions = mockMvc.perform(post("/update").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(customer)).header("Authorization", Mockito.anyString()));
		actions.andExpect(status().isForbidden());
	}

	@Test
	public void testViewDetailsSuccess() throws Exception {
		Mockito.when(customerService.viewDetails(Mockito.anyLong())).thenReturn(new Customer());
		Mockito.when(customerService.validateToken(Mockito.anyString())).thenReturn(true);
		ResultActions actions = mockMvc
				.perform(get("/viewDetails?customerId=1").header("Authorization", Mockito.anyString()));
		actions.andExpect(status().isOk());

	}

	@Test
	public void testViewDetailsFailure() throws Exception {
		Mockito.when(customerService.viewDetails(Mockito.anyLong())).thenReturn(new Customer());
		Mockito.when(customerService.validateToken(Mockito.anyString())).thenReturn(false);
		ResultActions actions = mockMvc
				.perform(get("/viewDetails?customerId=1").header("Authorization", Mockito.anyString()));
		actions.andExpect(status().isForbidden());

	}

	public static String asJsonString(Customer customer) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(customer);

	}
}
