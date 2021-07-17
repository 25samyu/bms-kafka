package com.kafkaexample.bms.portal.config;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.kafkaexample.bms.portal.model.Loan;
import com.kafkaexample.bms.portal.service.BmsPortalServiceDao;

public class RetrieveLoanConsumer {
	@Autowired
	BmsPortalServiceDao bmsPortalService;

	private CountDownLatch latch = new CountDownLatch(1);

	@KafkaListener(id = "${spring.kafka.retrieveLoanConsumer.groupId}", topics = { "${spring.kafka.topic.retrieveLoan}",
			"${spring.kafka.topic.failedRetrieveLoan}" }, containerFactory = "retrieveLoanKafkaListenerContainerFactory")
	public void receive(List<Loan> payload) {
		bmsPortalService.saveLoans(payload);
		latch.countDown();
	}
}
