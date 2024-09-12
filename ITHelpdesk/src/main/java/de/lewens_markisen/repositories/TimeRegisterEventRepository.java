package de.lewens_markisen.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.lewens_markisen.domain.Person;
import de.lewens_markisen.domain.TimeRegisterEvent;

public interface TimeRegisterEventRepository extends JpaRepository<TimeRegisterEvent, Long> {

	List<TimeRegisterEvent> findAllByPerson(Person person);

	//@formatter:off
	@Query(value = "SELECT r_start.id, r_start.version, r_start.created_date, r_start.last_modified_date, "
			+ "r_start.person_id, r_start.event_date, r_start.start_date, r_end.end_date\r\n"
			+ "	FROM time_register_event as r_start \r\n" 
			+ "	LEFT OUTER JOIN time_register_event as r_end \r\n"
			+ "		ON r_start.person_id = r_end.person_id\r\n"
			+ "			AND r_start.event_date = r_end.event_date AND r_end.end_date <> ''\r\n"
			+ "		WHERE r_start.person_id =?1 AND r_start.start_date <> '';"
			, nativeQuery = true)
	//@formatter:on
	List<TimeRegisterEvent> findAllByPersonWithoutDubl(Long personId);

}
