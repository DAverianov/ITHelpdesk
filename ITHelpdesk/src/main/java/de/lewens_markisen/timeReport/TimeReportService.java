package de.lewens_markisen.timeReport;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import de.lewens_markisen.person.Person;
import de.lewens_markisen.person.PersonService;
import de.lewens_markisen.timeRegisterEvent.TimeRegisterEvent;
import de.lewens_markisen.timeRegisterEvent.TimeRegisterEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimeReportService {

	private final TimeRegisterEventService timeRegisterEventService;
	private final PersonService personService;

	public Optional<List<TimeRegisterEvent>> findPersonEvents(String bcCode) {
		Optional<Person> personOpt = personService.findByBcCode(bcCode);
		if (personOpt.isPresent()) {
			return Optional.of(timeRegisterEventService.findAllByPersonWithoutDubl(personOpt.get()).get());

		} else {
			return Optional.empty();
		}
	}

	public Optional<TimeReport> createReport(String bcCode) {
		log.info("Create TimeReport for "+bcCode);
		//@formatter:off
		PeriodReport period = PeriodReport.PeriodReportMonth();
		Optional<Person> personOpt = personService.findByBcCode(bcCode);
		if (personOpt.isEmpty()) {
			return Optional.empty();
		} else {
			timeRegisterEventService.readEventsProPerson(personOpt.get());
			
			TimeReport timeReport = TimeReport.builder()
					.person(personOpt.get())
					.period(period)
					.timeRecords(timeRegisterEventService.findAllByPersonWithoutDubl(personOpt.get()).get())
					.build();
			timeReport.createWeeks();
			return Optional.of(timeReport);
		}
		//@formatter:on
		
	}
	public Optional<TimeReport> createReportCurrentUser() {
		Optional<String> bcCodeOpt = getUserBcCode();
		if (bcCodeOpt.isPresent()) {
			return createReport(bcCodeOpt.get());
		}
		else {
			return Optional.empty();
		}
	}

	public Optional<String> getUserBcCode() {
		if (SecurityContextHolder.getContext().getAuthentication()!=null) {
			Optional<Person> personOpt = personService.findByNameWithoutSpace(SecurityContextHolder.getContext().getAuthentication().getName());
			if (personOpt.isPresent()) {
				return Optional.of(personOpt.get().getBcCode());
			}
			else {
				return Optional.empty();
			}
		}
		else {
			return Optional.empty();
		}
	}

}
