package de.lewens_markisen.person;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.repository.local.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	@Override
	public Optional<Person> findById(Long id) {
		return personRepository.findById(id);
	}

	@Override
	public Page<Person> findAll(Pageable pageable) {
		return personRepository.findAll(pageable);
	}

	@Override
	public Page<Person> findAllActive(Pageable pageable) {
		return personRepository.findAllByFiringDate(pageable, LocalDate.of(1, 1, 1));
	}

	@Override
	public Page<Person> findAllByNameIsLikeIgnoreCase(Pageable pageable, String findField) {
		return personRepository.findAllByNameIsLikeIgnoreCase(pageable, findField);
	}

	@Override
	public Page<Person> findAllByNameIsLikeIgnoreCaseAndActive(	Pageable pageable, String findField) {
		return personRepository.findAllByNameIsLikeIgnoreCaseAndFiringDate(pageable, findField, LocalDate.of(1, 1, 1));
	}

	@Override
	public Optional<Person> findByNameForSearch(String name) {
		return personRepository.findByNameForSearch(name);
	}

	@Override
	public Person updatePerson(Person person) {
		return personRepository.save(person);
	}

	@Override
	public Person save(Person p) {
		return personRepository.save(p);
	}

	@Override
	public void delete(Person person) {
		personRepository.delete(person);
	}

	@Override
	public List<Person> findAllByBcCode(String bcCode) {
		return personRepository.findAllByBcCode(bcCode);
	}

}
