package de.lewens_markisen.timeReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.local_db.Log;
import de.lewens_markisen.domain.local_db.Person;
import de.lewens_markisen.domain.local_db.security.UserSpring;
import de.lewens_markisen.domain.local_db.time_register_event.TimeRegisterEvent;
import de.lewens_markisen.log.LogService;
import de.lewens_markisen.person.PersonService;
import de.lewens_markisen.security.LssUserService;
import de.lewens_markisen.security.UserSpringService;
import de.lewens_markisen.timeRegisterEvent.TimeRegisterEventService;
import de.lewens_markisen.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimeReportService {

	private final TimeRegisterEventService timeRegisterEventService;
	private final PersonService personService;
	private final LogService logService;
	private final UserSpringService userService;
	private final LssUserService lssUserService;

	public Optional<List<TimeRegisterEvent>> findPersonEvents(String bcCode) {
		PeriodReport period = PeriodReport.thisMonat();
		Optional<Person> personOpt = personService.findByBcCode(bcCode);
		if (personOpt.isPresent()) {
			return Optional.of(timeRegisterEventService.findAllByPerson(personOpt.get(), period).get());

		} else {
			return Optional.empty();
		}
	}

	public Optional<TimeReport> createReport(String bcCode) {
		//@formatter:off
		PeriodReport period = periodCurrentWithLastMonats();
		Optional<Person> personOpt = personService.findByBcCode(bcCode);
		if (personOpt.isEmpty()) {
			return Optional.empty();
		} else {
			return createReport(personOpt.get(), period);
		}
		//@formatter:on

	}
	public Optional<TimeReport> createReport(Person person, PeriodReport period) {
		//@formatter:off
		logRecord(person, period);
		timeRegisterEventService.readEventsProPerson(person, period);
		
		TimeReport timeReport = TimeReport.builder()
				.person(person)
				.period(period)
				.header(createHeader(person, period))
				.timeRecords(timeRegisterEventService.findAllByPersonAndMonth(person, period.getStart()))
				.build();
		timeReport.createReportRecords();
		timeReport.createGroup(1
				, (tr) -> tr.getYearWeek()
				, (tr) -> timeReport.startGroup(tr.getEventDate(), (ld) -> DateUtils.startWeekInMonat(ld))); 
		timeReport.createGroup(2
				, (tr) -> tr.getYearMonat()
				, (tr) -> timeReport.startGroup(tr.getEventDate(), (ld) -> DateUtils.startMonat(ld))); 
		return Optional.of(timeReport);
		//@formatter:on
	}
	
	private void logRecord(Person person, PeriodReport period) {
		log.info("Create TimeReport for " + person.getName());
		Optional<UserSpring> userOpt = userService.getCurrentUser();
		if (userOpt.isPresent()) {
			logService.save(Log.builder()
					.user(userOpt.get())
					.event("TimeReport")
					.description("time report for "+person.getName()+" "+period.toString())
					.build());
		}
	}

	private PeriodReport periodCurrentWithLastMonats() {
		//@formatter:off
		return PeriodReport.builder()
				.start(getStartDateReport())
				.end(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()))
				.build();
		//@formatter:on
	}

	private String createHeader(Person person, PeriodReport period) {
		//@formatter:off
		return "Benutzer: "+userService.getAuthenticationName()
			+" (" + person.getName() + " "+person.getBcCode()+") " 
			+ period.toString();
		//@formatter:on
	}

	private LocalDate getStartDateReport() {
		LocalDate now = LocalDate.now();
		if (now.getDayOfMonth() > 10) {
			return now.withDayOfMonth(1);
		} else {
			return now.minusMonths(1).withDayOfMonth(1);
		}
	}

	public Optional<TimeReport> createReportCurrentUser() {
		Optional<String> bcCodeOpt = getUserBcCode(userService.getAuthenticationName());
		if (bcCodeOpt.isPresent()) {
			return createReport(bcCodeOpt.get());
		} else {
			return Optional.empty();
		}
	}

	public Optional<String> getUserBcCode(String username) {
		if (!username.equals("")) {
			return lssUserService.getBcCodeByUsername(username);
		} else {
			log.info("Create TimeReport for " + username+". There is no BC Code for the user");
			return Optional.empty();
		}
	}

}
