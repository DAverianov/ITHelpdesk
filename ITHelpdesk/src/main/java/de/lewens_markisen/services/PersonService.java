package de.lewens_markisen.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.lewens_markisen.domain.Person;

public interface PersonService {

    Optional<Person> findOrCreate(String bcCode, String name);
    Optional<Person> findByBcCode(String bcCode);
    Person updatePerson(Person person);
	Optional<Person> findById(Long id);
	Page<Person> findAll(Pageable pageable);
}
