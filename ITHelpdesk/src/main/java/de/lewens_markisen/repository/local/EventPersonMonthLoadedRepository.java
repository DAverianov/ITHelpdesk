package de.lewens_markisen.repository.local;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.time_register_event.EventPersonMonthLoaded;

public interface EventPersonMonthLoadedRepository extends JpaRepository<EventPersonMonthLoaded, Long>{
	Optional<EventPersonMonthLoaded> findByPersonAndMonth(Person person, LocalDate monat);
}
