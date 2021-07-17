package com.kafkaexample.bms.portal.config;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.kafkaexample.bms.portal.service.BmsPortalServiceDao;

public class ApplyLoanMessageConsumer {
	@Autowired
	BmsPortalServiceDao bmsPortalService;

	private CountDownLatch latch = new CountDownLatch(1);

	@KafkaListener(id = "${spring.kafka.applyLoanMessageConsumer.groupId}", topics = {
			"${spring.kafka.topic.applyLoanMessage}",
			"${spring.kafka.topic.failedLoanMessage}" }, containerFactory = "applyLoanMessageKafkaListenerContainerFactory")
	public void receive(String[] payload) {
		bmsPortalService.getMessage(payload);
		latch.countDown();
	}
}
