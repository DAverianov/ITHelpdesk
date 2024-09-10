package de.lewens_markisen.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.Person;
import de.lewens_markisen.repositories.PersonRepository;

@Service
public class PersonServiceImpl implements PersonService {

	private final PersonRepository personRepository;

	public PersonServiceImpl(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	@Override
	public List<Person> findAll() {
		return personRepository.findAll();
	}

	@Override
	public Optional<Person> getPersonOrCreate(String bcCode, String name) {
		if (bcCode == "") {
			return Optional.empty();
		}
		Optional<Person> personFetch = personRepository.findBybcCode(bcCode);
		if (personFetch.isPresent()) {
			return Optional.of(personFetch.get());
		} else {
			//@formatter:off
			Person personNew = Person.builder()
					.bcCode(bcCode)
					.name(name)
					.build();
			Person personSaved = personRepository.save(personNew);
			return Optional.of(personSaved);
			//@formatter:on
		}
	}

}
