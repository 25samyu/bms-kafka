package com.kafkaexample.bms.portal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;

import com.kafkaexample.bms.portal.model.Loan;

public class ApplyLoanProducer {

	@Autowired
	@Qualifier("applyLoanProducerKafkaTemplate")
	private KafkaTemplate<String, Loan> applyLoanProducerKafkaTemplate;

	public void send(String topic, Loan payload) {
		applyLoanProducerKafkaTemplate.send(topic, payload);
	}
}
