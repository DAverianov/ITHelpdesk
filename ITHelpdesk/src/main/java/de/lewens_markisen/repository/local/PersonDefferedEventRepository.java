package de.lewens_markisen.repository.local;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.local_db.person.DefferedEvent;
import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.person.PersonDefferedEvent;
import de.lewens_markisen.domain.local_db.security.UserSpring;

public interface PersonDefferedEventRepository
		extends
			JpaRepository<PersonDefferedEvent, Long> {

	public List<PersonDefferedEvent> findAllByUserAndDefferedEvent(UserSpring user,
			DefferedEvent event);

	public List<PersonDefferedEvent> findAllByUserAndPersonAndDefferedEvent(
			UserSpring userSpring, Person person, DefferedEvent event);

	public List<PersonDefferedEvent> findAllByDefferedEventAndDone(DefferedEvent event, boolean done);

}
