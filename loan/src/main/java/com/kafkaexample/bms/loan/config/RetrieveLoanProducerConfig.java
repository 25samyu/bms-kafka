package com.kafkaexample.bms.loan.config;

import java.util.HashMap;
import java.util.List;
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

import com.kafkaexample.bms.loan.model.Loan;

@Configuration
@EnableKafka
public class RetrieveLoanProducerConfig {
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Bean
	public Map<String, Object> retrieveLoanProducerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		return props;
	}

	@Bean
	public ProducerFactory<String, List<Loan>> retrieveLoanProducerFactory() {
		return new DefaultKafkaProducerFactory<>(retrieveLoanProducerConfigs());
	}

	@Bean
	public KafkaTemplate<String, List<Loan>> retrieveLoanProducerKafkaTemplate() {
		return new KafkaTemplate<>(retrieveLoanProducerFactory());
	}

	@Bean
	public RetrieveLoanProducer retrieveLoanProducer() {
		return new RetrieveLoanProducer();
	}
}
