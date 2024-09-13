package de.lewens_markisen.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.lewens_markisen.person.Person;
import de.lewens_markisen.person.PersonService;
import de.lewens_markisen.timeRegisterEvent.TimeRegisterEvent;
import de.lewens_markisen.timeRegisterEvent.TimeRegisterEventService;
import de.lewens_markisen.timeReport.PeriodReport;
import de.lewens_markisen.timeReport.TimeReport;
import lombok.RequiredArgsConstructor;

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
		//@formatter:off
		PeriodReport period = PeriodReport.PeriodReportMonth();
		Optional<Person> personOpt = personService.findByBcCode(bcCode);
		if (personOpt.isEmpty()) {
			return Optional.empty();
		} else {
//			timeRegisterEventService.readEventsProPerson(personOpt.get());
			return Optional.of(TimeReport.builder()
					.person(personOpt.get())
					.period(period)
					.timeRecords(timeRegisterEventService.findAllByPersonWithoutDubl(personOpt.get()).get())
					.build());
		}
		//@formatter:on
	}

}
