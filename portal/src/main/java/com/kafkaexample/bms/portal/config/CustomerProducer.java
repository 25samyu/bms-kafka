package com.kafkaexample.bms.portal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;

import com.kafkaexample.bms.portal.model.Customer;

public class CustomerProducer {

	@Autowired
	@Qualifier("customerProducerKafkaTemplate")
	private KafkaTemplate<String, Customer> customerProducerKafkaTemplate;

	public void send(String topic, Customer payload) {
		customerProducerKafkaTemplate.send(topic, payload);
	}
}
