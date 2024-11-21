package de.lewens_markisen.person;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.person.PersonPresence;
import de.lewens_markisen.repository.local.PersonPresenceRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PersonPresenceServiceImpl implements PersonPresenceService{

	private final PersonPresenceRepository personPresenceRepository;
	
	@Override
	public List<PersonPresence> findAllByPresence(Boolean presence) {
		return personPresenceRepository.findAllByPresence(presence);
	}

	@Override
	public Optional<PersonPresence> findByPerson(Person person) {
		return personPresenceRepository.findByPerson(person);
	}

	@Override
	public void saveAll(List<PersonPresence> presences) {
		personPresenceRepository.saveAll(presences);
	}

}
