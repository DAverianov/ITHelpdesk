package de.lewens_markisen.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.Person;
import de.lewens_markisen.domain.TimeRegisterEvent;

public interface TimeRegisterEventRepository extends JpaRepository<TimeRegisterEvent, Long>{

	List<TimeRegisterEvent> findAllByPerson(Person person);

}
