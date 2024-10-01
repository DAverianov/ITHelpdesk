package de.lewens_markisen.timeReport;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import de.lewens_markisen.person.Person;
import de.lewens_markisen.timeRegisterEvent.TimeRegisterEvent;
import de.lewens_markisen.utils.DateUtils;

@ActiveProfiles("test")
class TimeReportTest {
	private final String BC_CODE = "645";

	@Test
	void createWeek_whenCreate_thenSorted() {
		Person person = Person.builder().name("user").bcCode(BC_CODE).build();
		PeriodReport period = PeriodReport.PeriodReportMonth();
		TimeReport timeReport = TimeReport.builder()
				.person(person)
				.period(period)
				.timeRecords(createTimeRecords(person))
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
