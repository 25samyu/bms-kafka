package com.kafkaexample.bms.loan.config;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.kafkaexample.bms.loan.model.Loan;
import com.kafkaexample.bms.loan.service.LoanServiceDao;

public class ApplyLoanConsumer {
	@Autowired
	LoanServiceDao loanService;

	private CountDownLatch latch = new CountDownLatch(1);

	@KafkaListener(id = "${spring.kafka.applyLoanConsumer.groupId}", topics = "${spring.kafka.topic.applyLoan}", containerFactory = "applyLoanConsumerKafkaListenerContainerFactory")
	public void receive(Loan payload) {
		loanService.applyLoan(payload);
		latch.countDown();
	}
}
