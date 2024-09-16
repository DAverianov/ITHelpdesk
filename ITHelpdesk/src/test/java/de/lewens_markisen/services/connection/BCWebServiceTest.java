package de.lewens_markisen.services.connection;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import de.lewens_markisen.person.Person;
import de.lewens_markisen.timeRegisterEvent.TimeRegisterEvent;

@ActiveProfiles("test")
@SpringBootTest()
class BCWebServiceTest {
	private final String BC_CODE = "645";
	@Autowired
	BCWebService bcWebService;

	@Test
	void readTimeRegisterEventsFromBC_whenRead_thenReceive() {
		Person person = Person.builder().name("user").bcCode(BC_CODE).build();
		Optional<List<TimeRegisterEvent>> events = bcWebService.readTimeRegisterEventsFromBC(person);
		assertThat(events.isPresent());
		assertThat(events.get()).isNotEmpty().hasAtLeastOneElementOfType(TimeRegisterEvent.class);
	}

	@Test
	void createTimeReport_whenQuery_thenReceive() {
		Person person = Person.builder().name("user").bcCode(BC_CODE).build();
		List<String> reportList = bcWebService.createTimeReport(person);
		assertThat(reportList).isNotEmpty().hasAtLeastOneElementOfType(String.class);
	}
	
	@Test
	void compoundDublRecords() {
		Person person = Person.builder().name("user").bcCode(BC_CODE).build();
		LocalDate eventDate = LocalDate.of(2024, 9, 16);
		List<TimeRegisterEvent> events = new ArrayList<TimeRegisterEvent>();
		//@formatter:off
		events.add(
			TimeRegisterEvent.builder()
				.person(person)
				.eventDate(eventDate)
				.startTime("7:00")
				.endTime("")
				.build());
		events.add(
				TimeRegisterEvent.builder()
				.person(person)
				.eventDate(eventDate)
				.startTime("")
				.endTime("17:00")
				.build());
		//@formatter:on
		bcWebService.compoundDublRecords(events);
		assertThat(events).isNotEmpty().hasSize(1);
		assertThat(events.get(0).getStartTime()).isEqualTo("7:00");
		assertThat(events.get(0).getEndTime()).isEqualTo("17:00");
	}

}
