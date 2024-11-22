package de.lewens_markisen.person;

import java.util.List;

import de.lewens_markisen.domain.local_db.person.DefferedEvent;
import de.lewens_markisen.domain.local_db.person.PersonDefferedEvent;
import de.lewens_markisen.domain.local_db.security.UserSpring;

public interface PersonDefferedEventService {
	public List<PersonDefferedEvent> findAllByUserAndDefferedEvent(UserSpring user, DefferedEvent event);
	public Boolean changeGlock(UserSpring userSpring, Long person_id);
	public void processEvents();
}
