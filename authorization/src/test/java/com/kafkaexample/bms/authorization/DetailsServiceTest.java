package com.kafkaexample.bms.authorization;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.kafkaexample.bms.authorization.model.UserCredentials;
import com.kafkaexample.bms.authorization.repository.UserCredentialsRepository;
import com.kafkaexample.bms.authorization.service.DetailsService;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class DetailsServiceTest {
	@InjectMocks
	private DetailsService detailsService;

	@Mock
	private UserCredentialsRepository userCredentialsRepository;

	@Test
	public void loadByUsernameSuccess() {
		Mockito.when(userCredentialsRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(new UserCredentials("test", "test", null)));
		assertNotNull(detailsService.loadUserByUsername(Mockito.anyString()));
	}

	@Test
	public void loadByUsernameFailure() {
		Mockito.when(userCredentialsRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		assertNull(detailsService.loadUserByUsername(Mockito.anyString()));
	}

	@Test(expected = RuntimeException.class)
	public void loadByUsernameException() {
		Mockito.when(userCredentialsRepository.findById(Mockito.anyString())).thenThrow(new RuntimeException());
		detailsService.loadUserByUsername(Mockito.anyString());
	}

	@Test
	public void saveCredentialsSuccess() {
		UserCredentials userCredentials = new UserCredentials("test", "test", null);
		Mockito.when(userCredentialsRepository.save(userCredentials)).thenReturn(userCredentials);
		assertTrue(detailsService.saveCredentials(userCredentials));
	}

	@Test
	public void saveCredentialsFailure() {
		UserCredentials userCredentials = new UserCredentials("test", "test", null);
		Mockito.when(userCredentialsRepository.save(userCredentials)).thenThrow(new RuntimeException());
		assertFalse(detailsService.saveCredentials(userCredentials));
	}
}
