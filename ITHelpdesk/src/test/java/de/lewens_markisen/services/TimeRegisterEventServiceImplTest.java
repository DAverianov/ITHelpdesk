package de.lewens_markisen.services;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.lewens_markisen.person.Person;
import de.lewens_markisen.person.PersonService;
import de.lewens_markisen.repository.PersonRepository;
import de.lewens_markisen.repository.TimeRegisterEventRepository;
import de.lewens_markisen.timeRegisterEvent.TimeRegisterEvent;
import de.lewens_markisen.timeRegisterEvent.TimeRegisterEventServiceImpl;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
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
		
		Optional<List<TimeRegisterEvent>> events = timeRegisterEventServiceImpl.readEventsProPerson(personOpt.get());
		assertThat(events).isNotEmpty();
	}
	
	@Test
	void findAllByPersonWithoutDubl_whenRequest_thenAnser() {
		Optional<Person> personOpt = personService.findOrCreate(BC_CODE, "TEST USER");
		Optional<List<TimeRegisterEvent>> events = timeRegisterEventServiceImpl.findAllByPersonWithoutDubl(personOpt.get());
		assertThat(events).isNotEmpty();
	}

}
