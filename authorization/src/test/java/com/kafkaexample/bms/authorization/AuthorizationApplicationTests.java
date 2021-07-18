package com.kafkaexample.bms.authorization;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.kafkaexample.bms.authorization.service.DetailsService;

@RunWith(Suite.class)

@Suite.SuiteClasses({ AuthorizationControllerTest.class, DetailsService.class })

public class AuthorizationApplicationTests {
}
