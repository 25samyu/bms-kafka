package com.kafkaexample.bms.loan;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({ LoanServiceControllerTest.class, LoanServiceDaoImplTest.class })

public class LoanApplicationTests {
}
