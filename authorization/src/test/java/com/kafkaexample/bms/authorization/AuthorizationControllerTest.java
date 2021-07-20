package com.kafkaexample.bms.authorization;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import com.kafkaexample.bms.authorization.repository.UserCredentialsRepository;
import com.kafkaexample.bms.authorization.service.DetailsService;
import com.kafkaexample.bms.authorization.service.JwtUtil;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
public class AuthorizationControllerTest {
	@Autowired
	private AuthorizationController authorizationController;
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	DetailsService detailsService;

	@Autowired
	UserCredentialsRepository userCredentialsRepository;

	@Autowired
	JwtUtil jwtUtil;

	private String TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyNjYyNzg2NSwiaWF0IjoxNjI2NjI2MDY1fQ.93fcPeGaZZC6meozhXmt0vRttMV-GFjv4j1g-3Iqn4k";

	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);

		this.mockMvc = MockMvcBuilders.standaloneSetup(authorizationController).build();

	}

	@Test
	public void testLoginSuccess() throws Exception {
		userCredentialsRepository.save(new UserCredentials("test", "test", null));
		ResultActions actions = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(new UserCredentials("test", "test", null))));
		actions.andExpect(status().isOk());

	}

	@Test
	public void testLoginFailure() throws Exception {
		ResultActions actions = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(new UserCredentials("", "", null))));
		actions.andExpect(status().isForbidden());

	}

	@Test
	public void testLoginPasswordFailure() throws Exception {
		userCredentialsRepository.save(new UserCredentials("test", "test", null));
		ResultActions actions = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(new UserCredentials("test", "admin", null))));
		actions.andExpect(status().isForbidden());

	}

	@Test
	public void testLoginExceptionFailure() throws Exception {
		ResultActions actions = mockMvc.perform(
				post("/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(new UserCredentials())));

		actions.andExpect(status().isInternalServerError());

	}

	@Test
	public void testValidateSuccess() throws Exception {
		ResultActions actions = mockMvc.perform(get("/validate").header("Authorization", TOKEN));
		actions.andExpect(status().isForbidden());

	}

	@Test
	public void testValidateFailure() throws Exception {
		ResultActions actions = mockMvc.perform(get("/validate").header("Authorization", "Bearer testing"));
		actions.andExpect(status().isForbidden());

	}

	@Test
	public void testValidateEmptyFailure() throws Exception {
		ResultActions actions = mockMvc.perform(get("/validate").header("Authorization", ""));
		actions.andExpect(status().isForbidden());

	}

	public static String asJsonString(UserCredentials userCredentials) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(userCredentials);

	}
}
