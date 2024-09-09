package de.lewens_markisen.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.lewens_markisen.connection.BCWebService;
import de.lewens_markisen.connection.ConnectionBC;
import de.lewens_markisen.domain.Person;
import de.lewens_markisen.domain.TimeRegisterEvent;
import de.lewens_markisen.repositories.PersonRepository;
import de.lewens_markisen.repositories.TimeRegisterEventRepository;

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
	public List<TimeRegisterEvent> findAll(Long personId) {
		Optional<Person> person = personRepository.findById(personId);
		if (person.isPresent()) {
			readEventsProPerson(person.get());
			return timeRegisterEventRepository.findAllByPerson(person.get());
		}
		else {
			return null;
		}
	}

	private void readEventsProPerson(Person person) {
		// delete all records
		List<TimeRegisterEvent> events = timeRegisterEventRepository.findAllByPerson(person);
		events.stream().forEach(e -> timeRegisterEventRepository.delete(e));
		// read from BC
		events = bcWebService.readTimeRegisterEventsFromBC(person);
		// save
		events.stream().forEach(e -> timeRegisterEventRepository.save(e));
	}

}
