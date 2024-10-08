package de.lewens_markisen.timeReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.localDb.Log;
import de.lewens_markisen.domain.localDb.Person;
import de.lewens_markisen.domain.localDb.TimeRegisterEvent;
import de.lewens_markisen.domain.localDb.security.UserSpring;
import de.lewens_markisen.log.LogService;
import de.lewens_markisen.person.PersonService;
import de.lewens_markisen.security.UserSpringServiceImpl;
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
	private final UserSpringServiceImpl userService;

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
		log.info("Create TimeReport for " + bcCode);
		Optional<UserSpring> userOpt = userService.getCurrentUser();
		if (userOpt.isPresent()) {
			logService.save(Log.builder()
					.user(userOpt.get())
					.event("TimeReport")
					.description("time report ")
					.build());
		}

		PeriodReport period = PeriodReport.builder()
				.start(getStartDateReport())
				.end(LocalDate.now())
				.build();
		Optional<Person> personOpt = personService.findByBcCode(bcCode);
		if (personOpt.isEmpty()) {
			return Optional.empty();
		} else {
			timeRegisterEventService.readEventsProPerson(personOpt.get(), period);
			
			TimeReport timeReport = TimeReport.builder()
					.person(personOpt.get())
					.period(period)
					.timeRecords(timeRegisterEventService.findAllByPerson(personOpt.get(), period).get())
					.build();
			timeReport.createReportRecords();
			timeReport.createGroup(1
					, (tr) -> tr.getYearWeek()
					, (tr) -> timeReport.startGroup(tr.getEventDate(), (ld) -> DateUtils.startWeekInMonat(ld))); 
//							ld.with(DayOfWeek.MONDAY)));
			timeReport.createGroup(2
					, (tr) -> tr.getYearMonat()
					, (tr) -> timeReport.startGroup(tr.getEventDate(), (ld) -> DateUtils.startMonat(ld))); 
			return Optional.of(timeReport);
		}
		//@formatter:on

	}
	
	private LocalDate getStartDateReport() {
		LocalDate now = LocalDate.now();
		if (now.getDayOfMonth()>10) {
			return now.withDayOfMonth(1);
		}
		else {
			return now.minusMonths(1).withDayOfMonth(1);
		}
	}

	public Optional<TimeReport> createReportCurrentUser() {
		Optional<String> bcCodeOpt = getUserBcCode();
		if (bcCodeOpt.isPresent()) {
			return createReport(bcCodeOpt.get());
		} else {
			return Optional.empty();
		}
	}

	public Optional<String> getUserBcCode() {
		String userName = userService.getAuthenticationName();
		if (!userName.equals("")) {
			Optional<Person> personOpt = personService
					.findByNameForSearch(Person.convertToNameForSearch(userName));
			if (personOpt.isPresent()) {
				return Optional.of(personOpt.get().getBcCode());
			} else {
				return Optional.empty();
			}
		} else {
			return Optional.empty();
		}
	}

}
