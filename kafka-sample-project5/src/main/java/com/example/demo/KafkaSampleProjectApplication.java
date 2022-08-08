package com.example.demo;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

import com.example.demo.kafka.KafkaConsumerService;

@SpringBootApplication
public class KafkaSampleProjectApplication {

	static KafkaConsumerService kafkaservice;
	
	public static void main(String[] args) {
		SpringApplication.run(KafkaSampleProjectApplication.class, args);
		kafkaservice.KafkaConsumerService();
	}
	

}
