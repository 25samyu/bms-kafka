package com.kafkaexample.bms.portal.config;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.kafkaexample.bms.portal.service.BmsPortalServiceDao;

public class CustomerMessageConsumer {
	@Autowired
	BmsPortalServiceDao bmsPortalService;

	private CountDownLatch latch = new CountDownLatch(1);

	@KafkaListener(id = "${spring.kafka.customerMessageConsumer.groupId}", topics = {
			"${spring.kafka.topic.registerCustomerMessage}", "${spring.kafka.topic.updateCustomerMessage}",
			"${spring.kafka.topic.failedCustomerMessage}" }, containerFactory = "customerMessageConsumerKafkaListenerContainerFactory")
	public void receive(String[] payload) {
		bmsPortalService.getMessage(payload);
		latch.countDown();
	}
}
