package com.kafkaexample.bms.portal.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkaexample.bms.portal.model.Loan;

@Configuration
@EnableKafka
public class RetrieveLoanConsumerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Bean
	public Map<String, Object> retrieveLoanConsumerConfigs() {

		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "RetrieveLoanConsumer");
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

		return props;
	}

	@Bean
	public ConsumerFactory<String, List<Loan>> retrieveLoanConsumerFactory() {

		ObjectMapper om = new ObjectMapper();
		JavaType type = om.getTypeFactory().constructParametricType(List.class, Loan.class);
		return new DefaultKafkaConsumerFactory<>(retrieveLoanConsumerConfigs(), new StringDeserializer(),
				new JsonDeserializer<List<Loan>>(type, om, false));

	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, List<Loan>> retrieveLoanKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, List<Loan>> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(retrieveLoanConsumerFactory());

		return factory;
	}

	@Bean
	public RetrieveLoanConsumer retrieveLoanConsumer() {
		return new RetrieveLoanConsumer();
	}
}