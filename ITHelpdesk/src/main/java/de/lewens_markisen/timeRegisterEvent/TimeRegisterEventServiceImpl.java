package de.lewens_markisen.timeRegisterEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.lewens_markisen.person.Person;
import de.lewens_markisen.repository.PersonRepository;
import de.lewens_markisen.repository.TimeRegisterEventRepository;
import de.lewens_markisen.services.connection.BCWebService;
import de.lewens_markisen.timeReport.PeriodReport;
import jakarta.transaction.Transactional;

@Service
public class TimeRegisterEventServiceImpl implements TimeRegisterEventService {

	private final TimeRegisterEventRepository timeRegisterEventRepository;
	private final PersonRepository personRepository;
	private final BCWebService bcWebService;

	public TimeRegisterEventServiceImpl(TimeRegisterEventRepository timeRegisterEventRepository,
			PersonRepository personRepository, BCWebService bcWebService) {
		this.timeRegisterEventRepository = timeRegisterEventRepository;
		this.personRepository = personRepository;
		this.bcWebService = bcWebService;
	}

	@Override
	@Transactional
	public Optional<List<TimeRegisterEvent>> findAll(Long personId) {
		Optional<List<TimeRegisterEvent>> result = Optional.empty();
		Optional<Person> person = personRepository.findById(personId);
		PeriodReport period = PeriodReport.builder().start(LocalDate.of(1,1,1)).end(LocalDate.now()).build();
		if (person.isPresent()) {
			readEventsProPerson(person.get(), period);
			result = Optional.of(timeRegisterEventRepository.findAllByPerson(person.get()));
		}
		return result;
	}

	@Override
	@Transactional
	public Optional<List<TimeRegisterEvent>> readEventsProPerson(Person person, PeriodReport period) {
		// delete all records
		List<TimeRegisterEvent> events = timeRegisterEventRepository.findAllByPerson(person);
		events.stream().forEach(e -> timeRegisterEventRepository.delete(e));
		// read from BC
		Optional<List<TimeRegisterEvent>> eventsBC = bcWebService.readTimeRegisterEventsFromBC(person, period);
		List<TimeRegisterEvent> eventsBCsaved = new ArrayList<TimeRegisterEvent>();
		// save
		if (eventsBC.isPresent()) {
			eventsBC.get().stream().forEach(e -> eventsBCsaved.add(timeRegisterEventRepository.save(e)));
			return Optional.of(eventsBCsaved);
		}
		else {
			return Optional.empty();
		}
	}

	@Override
	public Optional<List<TimeRegisterEvent>> findAllByPerson(Person person, PeriodReport period) {
		return Optional.of(timeRegisterEventRepository.findAllByPerson(person));
	}

}
