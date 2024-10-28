package de.lewens_markisen;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import de.lewens_markisen.storage.StorageService;

@SpringBootApplication
@EnableScheduling
public class ItHelpdeskApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItHelpdeskApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}

}
