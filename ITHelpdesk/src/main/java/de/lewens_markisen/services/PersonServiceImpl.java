package de.lewens_markisen.services;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.Person;
import de.lewens_markisen.repositories.PersonRepository;

@Service
public class PersonServiceImpl implements PersonService{

	private final PersonRepository personRepository;

	public PersonServiceImpl(
			PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	@Override
	public Iterable<Person> findAll() {
		return personRepository.findAll();
	}

}
