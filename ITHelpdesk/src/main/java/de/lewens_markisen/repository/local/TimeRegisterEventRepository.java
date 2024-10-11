package de.lewens_markisen.repository.local;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import de.lewens_markisen.domain.localDb.Person;
import de.lewens_markisen.domain.localDb.TimeRegisterEvent;

public interface TimeRegisterEventRepository extends JpaRepository<TimeRegisterEvent, Long> {
	List<TimeRegisterEvent> findAllByPerson(Person person);
	
// ToDo Add security for users with person_id = bcCode
//    @Query("select o from time_register_event o where o.id =?1 and " +
//            "(true = :#{hasAuthority('person.timeReport.run')} or o.person_id = ?#{principal?.person?.id})")
//	List<TimeRegisterEvent> findAllByPersonIDSecure(Long id);
}
