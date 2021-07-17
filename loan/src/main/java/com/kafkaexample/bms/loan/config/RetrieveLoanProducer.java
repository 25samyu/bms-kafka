package com.kafkaexample.bms.loan.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import com.kafkaexample.bms.loan.model.Loan;

public class RetrieveLoanProducer {
	@Autowired
	private KafkaTemplate<String, List<Loan>> retrieveLoanProducerKafkaTemplate;

	public void send(String topic, List<Loan> payload) {
		retrieveLoanProducerKafkaTemplate.send(topic, payload);
	}

}
