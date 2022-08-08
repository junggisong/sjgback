package com.example.demo;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

@SpringBootApplication
public class KafkaSampleProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaSampleProjectApplication.class, args);
	}
	
	@Bean
	public ApplicationRunner runner(KafkaMessageListenerContainer<String, String> kafkaMessageListenerContainer) {
		return args -> {
			kafkaMessageListenerContainer.start();
		};
	}

}
