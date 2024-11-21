package de.lewens_markisen.timeRegisterEvent;

import java.time.LocalDate;
import java.util.Optional;

import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.time_register_event.EventPersonMonth;
import de.lewens_markisen.domain.local_db.time_register_event.EventPersonMonthLoaded;

public interface EventPersonMonthLoadedService {
	public Optional<EventPersonMonthLoaded> findByPersonAndEventAndMonth(Person person, EventPersonMonth event, LocalDate month);

	public EventPersonMonthLoaded save(Person person, EventPersonMonth event, LocalDate month);

}
