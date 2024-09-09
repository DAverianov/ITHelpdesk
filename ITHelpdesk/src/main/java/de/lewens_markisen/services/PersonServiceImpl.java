package de.lewens_markisen.services;

import java.util.List;

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
	public List<Person> findAll() {
		return personRepository.findAll();
	}

}
