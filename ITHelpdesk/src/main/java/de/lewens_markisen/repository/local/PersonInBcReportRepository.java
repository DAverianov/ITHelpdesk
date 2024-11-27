package de.lewens_markisen.repository.local;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.time_register_event.PersonInBcReport;

public interface PersonInBcReportRepository extends JpaRepository<PersonInBcReport, Long> {
	Optional<PersonInBcReport> findByPersonAndMonth(Person person, LocalDate month);
	
}
