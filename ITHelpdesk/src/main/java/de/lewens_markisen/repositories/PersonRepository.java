package de.lewens_markisen.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import de.lewens_markisen.domain.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

	Optional<Person> findBybcCode(String bcCode);

}
