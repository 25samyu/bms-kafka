package com.kafkaexample.bms.customer.config;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

import com.kafkaexample.bms.customer.model.Customer;
import com.kafkaexample.bms.customer.service.CustomerServiceDao;

public class CustomerConsumer {
	@Autowired
	CustomerServiceDao customerService;

	private CountDownLatch latch = new CountDownLatch(1);

	@KafkaListener(id = "${spring.kafka.customerConsumer.groupId}", topics = { "${spring.kafka.topic.registerCustomer}",
			"${spring.kafka.topic.updateCustomer}" }, containerFactory = "customerConsumerKafkaListenerContainerFactory")
	public void receive(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, Customer payload) {
		customerService.registerOrUpdateCustomer(topic, payload);
		latch.countDown();
	}
}
