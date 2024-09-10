package de.lewens_markisen.services.connection;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import de.lewens_markisen.domain.Person;
import de.lewens_markisen.domain.TimeRegisterEvent;

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

}
