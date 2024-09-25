package de.lewens_markisen.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.lewens_markisen.services.connection.BCWebService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ScheduledTasks {
	private final BCWebService bcWebService;

	@Scheduled(cron = "0 1 3 * * ?") // jeden Tag um 03:01
	public void reportCurrentTime() {
		bcWebService.loadPersonFromBC();
	}

}
