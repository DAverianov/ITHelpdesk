package de.lewens_markisen.services;

import java.util.List;
import java.util.Optional;

import de.lewens_markisen.domain.Person;

public interface PersonService {

    List<Person> findAll();
    Optional<Person> getPersonOrCreate(String bcCode, String name);
	
}