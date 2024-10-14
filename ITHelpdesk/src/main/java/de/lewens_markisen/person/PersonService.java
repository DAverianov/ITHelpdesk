package de.lewens_markisen.person;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.lewens_markisen.domain.localDb.Person;

public interface PersonService {

    Optional<Person> findOrCreate(String bcCode, String name);
    Optional<Person> findByBcCode(String bcCode);
	Optional<Person> findById(Long id);
	Page<Person> findAll(Pageable pageable);
	Page<Person> findAllByNameIsLikeIgnoreCase(Pageable pageable, String findField);
	Optional<Person> findByNameForSearch(String name);
	
	Person save(Person p);
    Person updatePerson(Person person);
	void delete(Person person);
}
