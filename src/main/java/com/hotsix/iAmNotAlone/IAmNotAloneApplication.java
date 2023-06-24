package com.hotsix.iAmNotAlone;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class IAmNotAloneApplication {
	public static void main(String[] args) {
		SpringApplication.run(IAmNotAloneApplication.class, args);
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		// jenkins CICD Test
	}
}
