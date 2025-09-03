package ru.shop.filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafkaStreams
public class StreamFilterApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamFilterApplication.class, args);
	}

}
