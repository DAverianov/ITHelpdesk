package de.lewens_markisen.timeRegisterEvent;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import de.lewens_markisen.domain.local_db.Person;
import de.lewens_markisen.domain.local_db.time_register_event.PersonInBcReport;

public interface PersonInBcReportService {
	Optional<PersonInBcReport> findByPersonAndMonth(Person person, LocalDate month);
	PersonInBcReport save(PersonInBcReport personInBcReport);
}
