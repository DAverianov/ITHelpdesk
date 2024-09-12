package de.lewens_markisen.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.Person;
import de.lewens_markisen.domain.TimeRegisterEvent;
import de.lewens_markisen.repositories.PersonRepository;
import de.lewens_markisen.repositories.TimeRegisterEventRepository;
import de.lewens_markisen.services.connection.BCWebService;

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
	public Optional<List<TimeRegisterEvent>> findAll(Long personId) {
		Optional<List<TimeRegisterEvent>> result = Optional.empty();
		Optional<Person> person = personRepository.findById(personId);
		if (person.isPresent()) {
			readEventsProPerson(person.get());
			result = Optional.of(timeRegisterEventRepository.findAllByPerson(person.get()));
		}
		return result;
	}

	public Optional<List<TimeRegisterEvent>> readEventsProPerson(Person person) {
		// delete all records
		List<TimeRegisterEvent> events = timeRegisterEventRepository.findAllByPerson(person);
		events.stream().forEach(e -> timeRegisterEventRepository.delete(e));
		// read from BC
		Optional<List<TimeRegisterEvent>> eventsBC = bcWebService.readTimeRegisterEventsFromBC(person);
		// save
		if (eventsBC.isPresent()) {
			eventsBC.get().stream().forEach(e -> timeRegisterEventRepository.save(e));
		}
		return eventsBC;
	}

	@Override
	public Optional<List<TimeRegisterEvent>> findAllByPersonWithoutDubl(Person person) {
		return Optional.of(timeRegisterEventRepository.findAllByPersonWithoutDubl(person.getId()));
	}

}
