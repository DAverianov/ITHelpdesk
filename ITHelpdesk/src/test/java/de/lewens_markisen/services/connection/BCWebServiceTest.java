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

import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.time_register_event.TimeRegisterEvent;
import de.lewens_markisen.timeReport.PeriodReport;

@ActiveProfiles("test")
@SpringBootTest()
class BCWebServiceTest {
	private final String BC_CODE = "1071";

	@Autowired
	private BCWebServiceTimeRegisterEvent bcWebService;

	@Test
	void readTimeRegisterEventsFromBC_whenRead_thenReceive() {
		Person person = Person.builder().name("user").bcCode(BC_CODE).build();
		PeriodReport period = PeriodReport.builder()
				.start(LocalDate.of(2024, 11, 1))
				.end(LocalDate.of(2024, 11, 30)).build();
		Optional<List<TimeRegisterEvent>> events = bcWebService
				.readTimeRegisterEventsFromBC(person, period);
		assertThat(events.isPresent());
		assertThat(events.get()).isNotEmpty()
				.hasAtLeastOneElementOfType(TimeRegisterEvent.class);
	}

	@Test
	void compoundDublRecords() {
		Person person = Person.builder().name("user").bcCode(BC_CODE).build();
		LocalDate eventDate = LocalDate.of(2024, 9, 16);
		List<TimeRegisterEvent> events = new ArrayList<TimeRegisterEvent>();
		// @formatter:off
		events.add(TimeRegisterEvent.builder().person(person)
				.eventDate(eventDate).startTime("7:00").endTime("").build());
		events.add(TimeRegisterEvent.builder().person(person)
				.eventDate(eventDate).startTime("").endTime("17:00").build());
		// @formatter:on
		List<TimeRegisterEvent> eventsWithoutDoubl = bcWebService
				.compoundDublRecords(events);
		assertThat(eventsWithoutDoubl).isNotEmpty().hasSize(1);
		assertThat(eventsWithoutDoubl.get(0).getStartTime()).isEqualTo("7:00");
		assertThat(eventsWithoutDoubl.get(0).getEndTime()).isEqualTo("17:00");
	}

}
