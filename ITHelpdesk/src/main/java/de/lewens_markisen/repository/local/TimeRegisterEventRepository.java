package de.lewens_markisen.repository.local;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.localDb.Person;
import de.lewens_markisen.domain.localDb.TimeRegisterEvent;

public interface TimeRegisterEventRepository extends JpaRepository<TimeRegisterEvent, Long> {
	List<TimeRegisterEvent> findAllByPerson(Person person);
}
