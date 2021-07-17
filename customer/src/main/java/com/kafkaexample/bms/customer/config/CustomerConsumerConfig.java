package com.kafkaexample.bms.customer.config;

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

import com.kafkaexample.bms.customer.model.Customer;

@Configuration
@EnableKafka
public class CustomerConsumerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Bean
	public Map<String, Object> customerConsumerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "CustomerConsumer");
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

		return props;
	}

	@Bean
	public ConsumerFactory<String, Customer> customerConsumerFactory() {
		DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();

		Map<String, Class<?>> classMap = new HashMap<>();
		classMap.put("com.kafkaexample.bms.portal.model.Customer", Customer.class);
		typeMapper.setIdClassMapping(classMap);

		typeMapper.addTrustedPackages("*");

		JsonDeserializer<Customer> deserializer = new JsonDeserializer<>(Customer.class);
		deserializer.setTypeMapper(typeMapper);
		deserializer.setUseTypeMapperForKey(true);

		return new DefaultKafkaConsumerFactory<>(customerConsumerConfigs(), new StringDeserializer(), deserializer);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Customer> customerConsumerKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Customer> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(customerConsumerFactory());

		return factory;
	}

	@Bean
	public CustomerConsumer customerConsumer() {
		return new CustomerConsumer();
	}
}