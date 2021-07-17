package com.kafkaexample.bms.portal.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.kafkaexample.bms.portal.model.Loan;

@Configuration
@EnableKafka
public class ApplyLoanProducerConfig {
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Bean
	public Map<String, Object> applyLoanProducerConfigs() {

		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return props;
	}

	@Bean
	public ProducerFactory<String, Loan> applyLoanProducerFactory() {
		return new DefaultKafkaProducerFactory<>(applyLoanProducerConfigs());
	}

	@Bean("applyLoanProducerKafkaTemplate")
	public KafkaTemplate<String, Loan> applyLoanProducerKafkaTemplate() {
		return new KafkaTemplate<>(applyLoanProducerFactory());
	}

	@Bean
	public ApplyLoanProducer applyLoanProducer() {
		return new ApplyLoanProducer();
	}
}
