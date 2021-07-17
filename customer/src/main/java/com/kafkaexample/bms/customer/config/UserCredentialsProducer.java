package com.kafkaexample.bms.customer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;

import com.kafkaexample.bms.customer.model.UserCredentials;

public class UserCredentialsProducer {
	@Autowired
	@Qualifier("userCredentialsProducerKafkaTemplate")
	private KafkaTemplate<String, UserCredentials> userCredentialsProducerKafkaTemplate;

	public void send(String topic, UserCredentials payload) {
		userCredentialsProducerKafkaTemplate.send(topic, payload);
	}

}
