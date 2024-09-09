package de.lewens_markisen.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.Person;
import de.lewens_markisen.domain.TimeRegisterEvent;
import de.lewens_markisen.repositories.PersonRepository;
import de.lewens_markisen.repositories.TimeRegisterEventRepository;

@Service
public class TimeRegisterEventServiceImpl implements TimeRegisterEventService {

	private final TimeRegisterEventRepository timeRegisterEventRepository;
	private final PersonRepository personRepository;

	public TimeRegisterEventServiceImpl(TimeRegisterEventRepository timeRegisterEventRepository,
			PersonRepository personRepository) {
		this.timeRegisterEventRepository = timeRegisterEventRepository;
		this.personRepository = personRepository;
	}

	@Override
	public Iterable<TimeRegisterEvent> findAll(Long personId) {
		Optional<Person> person = personRepository.findById(personId);
		if (person.isPresent()) {
			return timeRegisterEventRepository.findAllByPerson(person.get());
		}
		else {
			return null;
		}
	}

}
