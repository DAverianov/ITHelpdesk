package de.lewens_markisen.timeReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.local_db.Log;
import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.security.UserSpring;
import de.lewens_markisen.domain.local_db.time_register_event.DayArt;
import de.lewens_markisen.domain.local_db.time_register_event.PersonInBcReport;
import de.lewens_markisen.domain.local_db.time_register_event.TimeRegisterEvent;
import de.lewens_markisen.log.LogService;
import de.lewens_markisen.person.PersonService;
import de.lewens_markisen.security.LssUserService;
import de.lewens_markisen.security.UserSpringService;
import de.lewens_markisen.timeRegisterEvent.DayArtService;
import de.lewens_markisen.timeRegisterEvent.PersonInBcReportService;
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
	private final PersonInBcReportService personInBcReportService;
	private final DayArtService dayArtService;

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
			.personInBcReportLastMonat(personInBcReportService.findByPersonAndMonth(person, period.getStart().minusMonths(1)))
			.personInBcReport(personInBcReportService.findByPersonAndMonth(person, period.getStart()))
			.build();
		timeReport.createReportRecords();
		calculatePause(timeReport);

		timeReport.createGroup(1
			, (tr) -> tr.getYearWeek()
			, (tr) -> timeReport.startGroup(tr.getEventDate(), (ld) -> DateUtils.startWeekInMonat(ld))); 
		timeReport.createGroup(2
			, (tr) -> tr.getYearMonat()
			, (tr) -> timeReport.startGroup(tr.getEventDate(), (ld) -> DateUtils.startMonat(ld))); 
		return Optional.of(timeReport);
		//@formatter:on
	}

	private void calculatePause(TimeReport timeReport) {
		for (TimeReportRecord rec: timeReport.getRecordsWithGroups()) {
			rec.setBcReportZeitNachweisDateDescription(timeReport.getDateTableRecord(rec.getEventDate()));
			if (rec.getBcReportZeitNachweisDateDescription().isPresent()) {
				rec.setPause(getPause(rec.getBcReportZeitNachweisDateDescription().get().getGcodTAZ()));
			}
		}
	}

	private Long getPause(String tagCode) {
		if (tagCode.isBlank()) {
			return 0l;
		}
		Optional<DayArt> pauseOpt = dayArtService.findByName(tagCode);
		if (pauseOpt.isEmpty()) {
			dayArtService.save(DayArt.builder().name(tagCode).build());
			return 0l;
		}
		return (long) pauseOpt.get().getMinuten();
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
		LocalDate start = getStartDateReport();
		return PeriodReport.builder()
				.start(start)
				.end(DateUtils.endMonat(start))
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
		if (now.getDayOfMonth() > 5) {
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
