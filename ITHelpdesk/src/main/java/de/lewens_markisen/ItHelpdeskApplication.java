package de.lewens_markisen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ItHelpdeskApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItHelpdeskApplication.class, args);
	}

}
