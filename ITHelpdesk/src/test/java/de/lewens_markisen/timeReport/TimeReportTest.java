package de.lewens_markisen.timeReport;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import de.lewens_markisen.domain.local_db.Person;
import de.lewens_markisen.domain.local_db.time_register_event.PersonInBcReport;
import de.lewens_markisen.domain.local_db.time_register_event.TimeRegisterEvent;
import de.lewens_markisen.person.PersonService;
import de.lewens_markisen.timeRegisterEvent.PersonInBcReportService;
import de.lewens_markisen.utils.DateUtils;

@ActiveProfiles("test")
@SpringBootTest
class TimeReportTest {
	private final String BC_CODE = "1071";
	@Autowired
	private PersonInBcReportService personInBcReportService;
	@Autowired
	private PersonService personService;

	@Test
	void createWeek_whenCreate_thenSorted() {
//		Person person = Person.builder().name("user").bcCode(BC_CODE).build();
		Optional<Person> personOpt = personService.findByBcCode(BC_CODE);
		PeriodReport period = PeriodReport.periodReportMonth();
		Optional<PersonInBcReport> personInBcRepOpt = personInBcReportService.findByPersonAndMonth(personOpt.get(), period.getStart().minusMonths(1));
		
		TimeReport timeReport = TimeReport.builder()
				.person(personOpt.get())
				.period(period)
				.personInBcReportLastMonat(personInBcRepOpt)
				.timeRecords(createTimeRecords(personOpt.get()))
				.build();
		timeReport.createReportRecords();
		//@formatter:off
		timeReport.createGroup(1
				, (tr) -> tr.getYearWeek()
				, (tr) -> timeReport.startGroup(tr.getEventDate(), (ld) -> ld.with(TemporalAdjusters.previous(DayOfWeek.MONDAY))));
		timeReport.createGroup(2
				, (tr) -> tr.getYearMonat()
				, (tr) -> timeReport.startGroup(tr.getEventDate(), (ld) -> DateUtils.startMonat(ld))); 
		//@formatter:on
		
		assertThat(timeReport.getRecordsWithGroups()).isNotEmpty();
		assertThat(timeReport.getRecordsWithGroups()).hasSize(5);
		assertThat(timeReport.getUrlaubSaldo()).isEqualTo("11,0");
	}

	private List<TimeRegisterEvent> createTimeRecords(Person person) {
		List<TimeRegisterEvent> timeRecords = new ArrayList<TimeRegisterEvent>();
		timeRecords.add(TimeRegisterEvent.builder()
				.person(person)
				.eventDate(LocalDate.of(2024, 9, 01))
				.build());
		timeRecords.add(TimeRegisterEvent.builder()
				.person(person)
				.eventDate(LocalDate.of(2024, 9, 10))
				.build());
		return timeRecords;
	}

}
