package de.lewens_markisen.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.lewens_markisen.domain.Person;
import de.lewens_markisen.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

	private final PersonRepository personRepository;

	@Override
	public Optional<Person> findOrCreate(String bcCode, String name) {
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

	@Override
	public Optional<Person> findByBcCode(String bcCode) {
		return personRepository.findBybcCode(bcCode);
	}

    @Transactional()
    public Person updatePerson(Person person) {
        personRepository.findById(person.getId());
        return personRepository.save(person);
    }

	@Override
	public Optional<Person> findById(Long id) {
		return personRepository.findById(id);
	}

	@Override
	public Page<Person> findAll(Pageable pageable) {
		return personRepository.findAll(pageable);
	}

}
