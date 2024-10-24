package de.lewens_markisen.timeRegisterEvent;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.local_db.Person;
import de.lewens_markisen.domain.local_db.time_register_event.PersonInBcReport;
import de.lewens_markisen.repository.local.PersonInBcReportRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PersonInBcReportServiceImpl implements PersonInBcReportService{
	private final PersonInBcReportRepository personInBcReportRepository;
	
	@Override
	public Optional<PersonInBcReport> findByPersonAndMonth(Person person, LocalDate month) {
		return personInBcReportRepository.findByPersonAndMonth(person, month);
	}

	@Override
	public PersonInBcReport save(PersonInBcReport personInBcReport) {
		Optional<PersonInBcReport> persOpt = findByPersonAndMonth(personInBcReport.getPerson(), personInBcReport.getMonth());
		if (persOpt.isPresent()) {
			PersonInBcReport pers = persOpt.get();
			pers.setAttribute(personInBcReport.getAttribute());
			pers.setDateTable(personInBcReport.getDateTable());
			pers.setSaldo(personInBcReport.getSaldo());
			return personInBcReportRepository.save(pers);
		}
		else {
			return personInBcReportRepository.save(personInBcReport);
		}
	}

}
