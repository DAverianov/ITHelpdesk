package de.lewens_markisen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.lewens_markisen.person.Person;
import de.lewens_markisen.timeRegisterEvent.TimeRegisterEvent;

public interface TimeRegisterEventRepository extends JpaRepository<TimeRegisterEvent, Long> {

	List<TimeRegisterEvent> findAllByPerson(Person person);

}
