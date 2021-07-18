package com.kafkaexample.bms.authorization;

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
import com.kafkaexample.bms.authorization.controller.AuthorizationController;
import com.kafkaexample.bms.authorization.model.UserCredentials;
import com.kafkaexample.bms.authorization.service.DetailsService;
import com.kafkaexample.bms.authorization.service.JwtUtil;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
public class AuthorizationControllerTest {
	@InjectMocks
	private AuthorizationController authorizationController;
	@Autowired
	private MockMvc mockMvc;

	@Mock
	DetailsService detailsService;

	@Mock
	JwtUtil jwtUtil;

	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);

		this.mockMvc = MockMvcBuilders.standaloneSetup(authorizationController).build();

	}

	@Test
	public void testLoginFailure() throws Exception {
		Mockito.when(detailsService.loadUserByUsername(Mockito.anyString())).thenReturn(null);
		ResultActions actions = mockMvc.perform(
				post("/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(new UserCredentials())));
		actions.andExpect(status().isForbidden());

	}

//	@Test
//	public void testLoginExceptionFailure() throws Exception {
//		Mockito.when(detailsService.loadUserByUsername(Mockito.anyString())).thenThrow(new RuntimeException());
//		ResultActions actions = mockMvc.perform(
//				post("/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(new UserCredentials())));
//		actions.andExpect(status().isInternalServerError());
//
//	}

	public static String asJsonString(UserCredentials userCredentials) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(userCredentials);

	}
}
