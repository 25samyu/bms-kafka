package com.kafkaexample.bms.loan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

public class ApplyLoanMessageProducer {
	@Autowired
	private KafkaTemplate<String, String[]> applyLoanMessageProducerKafkaTemplate;

	public void send(String topic, String[] payload) {
		applyLoanMessageProducerKafkaTemplate.send(topic, payload);
	}

}
