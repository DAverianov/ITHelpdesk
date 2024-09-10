package de.lewens_markisen.services;

import java.util.List;
import java.util.Optional;

import de.lewens_markisen.domain.Person;
import de.lewens_markisen.domain.TimeRegisterEvent;

public interface TimeRegisterEventService {

	Optional<List<TimeRegisterEvent>> findAll(Long personId);
	Optional<String> readEventsProPerson(Person person);
}
