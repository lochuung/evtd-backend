package com.evtd.dictionary;

import com.evtd.dictionary.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DictionaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DictionaryApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(UserService userService) {
		return args -> {
			userService.createTestingData();
		};
	}
}
