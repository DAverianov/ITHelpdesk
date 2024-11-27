package de.lewens_markisen.timeRegisterEvent;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.time_register_event.TimeRegisterEvent;
import de.lewens_markisen.timeReport.PeriodReport;

public interface TimeRegisterEventService {
	public void readEventsProPerson(Person person, PeriodReport period);
	public Optional<List<TimeRegisterEvent>> findAllByPerson(Person person, PeriodReport period);
	public List<TimeRegisterEvent> findAllByPersonAndMonth(Person person, LocalDate month);
}
