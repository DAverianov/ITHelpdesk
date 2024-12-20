package de.lewens_markisen.repository.local;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.time_register_event.TimeRegisterEvent;

public interface TimeRegisterEventRepository extends JpaRepository<TimeRegisterEvent, Long> {
	
//    @Query("select tRE from TimeRegisterEvent tRE where tRE.person.id =?1 and " +
//            "(true = :#{hasAuthority('person.timeReport.run')} or tRE.person.id = ?#{principal?.person?.id})")
	List<TimeRegisterEvent> findAllByPerson(Person person);

	List<TimeRegisterEvent> findAllByPersonAndMonth(Person person, LocalDate month);
}
