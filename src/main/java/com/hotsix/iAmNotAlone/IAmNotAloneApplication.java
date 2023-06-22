package com.hotsix.iAmNotAlone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IAmNotAloneApplication {
	public static void main(String[] args) {
		SpringApplication.run(IAmNotAloneApplication.class, args);
	}
}
