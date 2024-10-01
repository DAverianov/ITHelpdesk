package de.lewens_markisen.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
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
import de.lewens_markisen.timeReport.PeriodReport;
import de.lewens_markisen.utils.DateUtils;

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
		PeriodReport period = PeriodReport.thisMonat();
		
		Optional<Person> personOpt = personService.findOrCreate(BC_CODE, "TEST USER");
		assertThat(personOpt.isPresent());
		
		Optional<List<TimeRegisterEvent>> events = timeRegisterEventServiceImpl.readEventsProPerson(personOpt.get(), period);
		assertThat(events).isNotEmpty();
	}
	
	@Test
	void findAllByPerson_whenRequest_thenAnser() {
		PeriodReport period = PeriodReport.thisMonat();
		
		Optional<Person> personOpt = personService.findOrCreate(BC_CODE, "TEST USER");
		Optional<List<TimeRegisterEvent>> events = timeRegisterEventServiceImpl.findAllByPerson(personOpt.get(), period);
		assertThat(events).isNotEmpty();
	}

}
