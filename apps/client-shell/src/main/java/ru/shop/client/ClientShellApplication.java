package ru.shop.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class ClientShellApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientShellApplication.class, args);
		System.out.println("""
      Добро пожаловать в CLIENT API (Spring Shell)
      Введите 'help' для списка команд, 'quit' для выхода.
      	""");
	}

}
