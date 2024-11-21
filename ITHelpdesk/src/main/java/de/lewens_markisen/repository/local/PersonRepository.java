package de.lewens_markisen.repository.local;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import de.lewens_markisen.domain.local_db.person.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

	public Optional<Person> findBybcCode(String bcCode);
	
	@Transactional(readOnly = true)
	@Cacheable("persons")
	public Page<Person> findAll(Pageable pageable) throws DataAccessException;

	@Transactional(readOnly = true)
	public Optional<Person> findByNameForSearch(String name);

	public Page<Person> findAllByNameIsLikeIgnoreCase(Pageable pageable, String findField);

	public List<Person> findAllByBcCode(String bcCode);

}
