package de.lewens_markisen.person;

import java.util.List;
import java.util.Optional;

import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.person.PersonPresence;

public interface PersonPresenceService {
	public List<PersonPresence> findAllByPresence(Boolean presence);
	public Optional<PersonPresence> findByPerson(Person person);
	public void saveAll(List<PersonPresence> presences);

}
