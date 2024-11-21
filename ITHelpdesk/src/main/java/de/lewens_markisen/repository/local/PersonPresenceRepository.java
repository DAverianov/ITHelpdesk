package de.lewens_markisen.repository.local;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.person.PersonPresence;

public interface PersonPresenceRepository extends JpaRepository<PersonPresence, Long>{

	public List<PersonPresence> findAllByPresence(Boolean presence);

	public Optional<PersonPresence> findByPerson(Person person);

}
