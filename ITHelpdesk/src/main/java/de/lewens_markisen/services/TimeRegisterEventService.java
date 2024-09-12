package de.lewens_markisen.services;

import java.util.List;
import java.util.Optional;

import de.lewens_markisen.domain.Person;
import de.lewens_markisen.domain.TimeRegisterEvent;

public interface TimeRegisterEventService {

	Optional<List<TimeRegisterEvent>> findAll(Long personId);
	Optional<List<TimeRegisterEvent>> readEventsProPerson(Person person);
	Optional<List<TimeRegisterEvent>> findAllByPersonWithoutDubl(Person person);
}
