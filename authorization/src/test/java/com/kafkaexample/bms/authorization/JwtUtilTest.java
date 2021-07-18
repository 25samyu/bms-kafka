package com.kafkaexample.bms.authorization;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.kafkaexample.bms.authorization.repository.UserCredentialsRepository;
import com.kafkaexample.bms.authorization.service.DetailsService;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class JwtUtilTest {
	@InjectMocks
	private DetailsService detailsService;

	@Mock
	private UserCredentialsRepository userCredentialsRepository;

}
