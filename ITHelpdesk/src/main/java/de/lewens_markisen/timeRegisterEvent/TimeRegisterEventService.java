package de.lewens_markisen.timeRegisterEvent;

import java.util.List;
import java.util.Optional;

import de.lewens_markisen.person.Person;
import de.lewens_markisen.timeReport.PeriodReport;

public interface TimeRegisterEventService {

	Optional<List<TimeRegisterEvent>> findAll(Long personId);
	Optional<List<TimeRegisterEvent>> readEventsProPerson(Person person, PeriodReport period);
	Optional<List<TimeRegisterEvent>> findAllByPerson(Person person, PeriodReport period);
}
