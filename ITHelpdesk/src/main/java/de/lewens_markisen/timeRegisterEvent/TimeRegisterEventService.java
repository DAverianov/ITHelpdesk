package de.lewens_markisen.timeRegisterEvent;

import java.util.List;
import java.util.Optional;

import de.lewens_markisen.person.Person;

public interface TimeRegisterEventService {

	Optional<List<TimeRegisterEvent>> findAll(Long personId);
	Optional<List<TimeRegisterEvent>> readEventsProPerson(Person person);
	Optional<List<TimeRegisterEvent>> findAllByPersonWithoutDubl(Person person);
}
