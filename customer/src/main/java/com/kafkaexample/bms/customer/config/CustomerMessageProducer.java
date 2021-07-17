package com.kafkaexample.bms.customer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;

public class CustomerMessageProducer {
	@Autowired
	@Qualifier("customerMessageProducerKafkaTemplate")
	private KafkaTemplate<String, String[]> customerMessageProducerKafkaTemplate;

	public void send(String topic, String[] payload) {
		customerMessageProducerKafkaTemplate.send(topic, payload);
	}

}
