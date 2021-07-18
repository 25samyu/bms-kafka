package com.kafkaexample.bms.customer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({ CustomerServiceControllerTest.class, CustomerServiceDaoImplTest.class })

public class CustomerApplicationTests {
}
