package com.kafkaexample.bms.authorization.config;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

import com.kafkaexample.bms.authorization.model.UserCredentials;
import com.kafkaexample.bms.authorization.service.DetailsService;

public class UserCredentialsConsumer {
	@Autowired
	DetailsService detailsService;

	private CountDownLatch latch = new CountDownLatch(1);

	@KafkaListener(id = "${spring.kafka.userCredentialsConsumer.groupId}", topics = "${spring.kafka.topic.userCredentials}", containerFactory = "userCredentialsConsumerKafkaListenerContainerFactory")
	public void receive(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, UserCredentials payload) {
		detailsService.saveCredentials(payload);
		latch.countDown();
	}
}
