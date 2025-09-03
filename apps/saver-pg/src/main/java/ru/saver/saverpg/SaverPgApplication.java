package ru.saver.saverpg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class SaverPgApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaverPgApplication.class, args);
	}

}
