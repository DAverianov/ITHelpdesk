package de.lewens_markisen.repository.local;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import de.lewens_markisen.domain.local_db.person.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

	public Optional<Person> findBybcCode(String bcCode);

	public Optional<Person> findByIdCard(String idCard);
	
	@Transactional(readOnly = true)
	@Cacheable("persons")
	public Page<Person> findAll(Pageable pageable) throws DataAccessException;

	@Transactional(readOnly = true)
	public Optional<Person> findByNameForSearch(String name);

	public List<Person> findAllByBcCode(String bcCode);

	public Page<Person> findAllByNameIsLikeIgnoreCase(Pageable pageable, String findField);

	@Query("SELECT p FROM Person p WHERE (p.firingDate is null or p.firingDate = :firingDate)")
	public Page<Person> findAllByFiringDate(Pageable pageable, LocalDate firingDate);

	@Query("SELECT p FROM Person p WHERE (lower(p.name) like :findField) and (p.firingDate is null or p.firingDate = :firingDate)")
	public Page<Person> findAllByNameIsLikeIgnoreCaseAndFiringDate(
			Pageable pageable, String findField, LocalDate firingDate);

}
