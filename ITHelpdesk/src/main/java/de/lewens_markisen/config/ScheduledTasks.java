package de.lewens_markisen.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.lewens_markisen.email.EmailLetterService;
import de.lewens_markisen.log.LogService;
import de.lewens_markisen.person.PersonDefferedEventService;
import de.lewens_markisen.security.RememberMeService;
import de.lewens_markisen.services.connection.BCWebServiceTimeRegisterEvent;
import de.lewens_markisen.services.connection.BCWebServiceLoadPerson;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ScheduledTasks {
	private final BCWebServiceLoadPerson bcWebServiceLoadPerson;
	private final BCWebServiceTimeRegisterEvent bcWebServiceTimeRegisterEvent;
	private final LogService logService;
	private final RememberMeService rememberMeService;
	private final PersonDefferedEventService personDefferedEventService;
	private final EmailLetterService emailLetterService;

	@Scheduled(cron = "0 1 3 * * ?") // every Day in 03:01
	public void localPersonsFromBC() {
		bcWebServiceLoadPerson.loadPersonFromBC();
	}

	@Scheduled(cron = "0 2 3 * * ?") // every Day in 03:02
	public void deleteLogAltRecords() {
		logService.deleteAltRecords();
	}

	@Scheduled(cron = "0 3 3 * * ?") // every Day in 03:03
	public void deleteRememberMe() {
		rememberMeService.deleteAllRecords();
	}
	
	@Scheduled(cron =  "0 */5 6-18 ? * *") // every 5 min
	public void readPersonPresence() {
		bcWebServiceTimeRegisterEvent.readPersonPresence();
	}
	
	@Scheduled(cron =  "0 */5 6-18 ? * *") // every 5 min
	public void processPersonDefferedEvents() {
		personDefferedEventService.processEvents();
	}
	
	@Scheduled(cron =  "0 */1 6-18 ? * *") // every 5 min
	public void sendEmails() {
		emailLetterService.sendAll();
	}

}
