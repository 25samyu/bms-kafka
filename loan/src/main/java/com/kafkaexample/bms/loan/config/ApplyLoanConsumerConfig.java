package com.kafkaexample.bms.loan.config;

import java.util.HashMap;
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
import org.springframework.kafka.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.kafkaexample.bms.loan.model.Loan;

@Configuration
@EnableKafka
public class ApplyLoanConsumerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Bean
	public Map<String, Object> applyLoanConsumerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "ApplyLoanConsumer");
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

		return props;
	}

	@Bean
	public ConsumerFactory<String, Loan> applyLoanConsumerFactory() {
		DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();

		Map<String, Class<?>> classMap = new HashMap<>();
		classMap.put("com.kafkaexample.bms.portal.model.Loan", Loan.class);
		typeMapper.setIdClassMapping(classMap);

		typeMapper.addTrustedPackages("*");

		JsonDeserializer<Loan> deserializer = new JsonDeserializer<>(Loan.class);
		deserializer.setTypeMapper(typeMapper);
		deserializer.setUseTypeMapperForKey(true);

		return new DefaultKafkaConsumerFactory<>(applyLoanConsumerConfigs(), new StringDeserializer(), deserializer);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Loan> applyLoanConsumerKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Loan> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(applyLoanConsumerFactory());

		return factory;
	}

	@Bean
	public ApplyLoanConsumer applyLoanConsumer() {
		return new ApplyLoanConsumer();
	}
}