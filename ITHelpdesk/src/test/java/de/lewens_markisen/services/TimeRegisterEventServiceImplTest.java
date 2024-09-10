package de.lewens_markisen.services;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import de.lewens_markisen.domain.Person;
import de.lewens_markisen.domain.TimeRegisterEvent;
import de.lewens_markisen.repositories.PersonRepository;
import de.lewens_markisen.repositories.TimeRegisterEventRepository;

@ActiveProfiles("test")
@SpringBootTest
class TimeRegisterEventServiceImplTest {
	private final String BC_CODE = "645";
	@Autowired
	TimeRegisterEventServiceImpl timeRegisterEventServiceImpl;
	@Autowired
	PersonRepository personRepository;
	@Autowired
	PersonService personService;
	@Autowired
	TimeRegisterEventRepository timeRegisterEventRepository;

	@Test
	void readEventsProPerson_whenTimeEventsQuery_thenReceiveAndWrite() {
		Optional<Person> personOpt = personService.findOrCreate(BC_CODE, "TEST USER");
		assertThat(personOpt.isPresent());
		
		timeRegisterEventServiceImpl.readEventsProPerson(personOpt.get());
		List<TimeRegisterEvent> timeEvents = timeRegisterEventRepository.findAllByPerson(personOpt.get());
		
		assertThat(timeEvents).isNotEmpty().hasAtLeastOneElementOfType(TimeRegisterEvent.class);
	}

}
