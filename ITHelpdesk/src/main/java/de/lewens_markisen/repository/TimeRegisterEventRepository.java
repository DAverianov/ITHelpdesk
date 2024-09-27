package de.lewens_markisen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.lewens_markisen.person.Person;
import de.lewens_markisen.timeRegisterEvent.TimeRegisterEvent;

public interface TimeRegisterEventRepository extends JpaRepository<TimeRegisterEvent, Long> {

	List<TimeRegisterEvent> findAllByPerson(Person person);

	//@formatter:off
	@Query(value = "SELECT distinct \r\n"
			+ "		r_start.id, r_start.person_id, r_start.event_date, r_start.start_time, r_end.end_time as end_time, "
			+ "		r_start.created_date, \r\n"
			+ "		r_start.last_modified_date, r_start.version\r\n"
			+ "\r\n"
			+ "	FROM (\r\n"
			+ "		SELECT min(id) as id,\r\n"
			+ "			person_id, event_date, min(start_time) as start_time, min(created_date) as created_date,\r\n"
			+ "			min(last_modified_date) as last_modified_date, "
			+ "			max(version) as version\r\n"
			+ "\r\n"
			+ "		FROM public.time_register_event\r\n"
			+ "		where person_id = ?1\r\n"
			+ "		group by person_id, event_date, end_time\r\n"
			+ "		) as r_start  \r\n"
			+ "	LEFT OUTER JOIN time_register_event as r_end \r\n"
			+ "		ON r_start.person_id = r_end.person_id\r\n"
			+ "			AND r_start.event_date = r_end.event_date AND r_end.end_time <> ''\r\n"
			+ "		WHERE r_start.person_id = ?1 AND r_start.start_time <> ''\r\n"
			+ "		order by event_date"
			, nativeQuery = true)
	//@formatter:on
	List<TimeRegisterEvent> findAllByPersonWithoutDubl(Long personId);

}
