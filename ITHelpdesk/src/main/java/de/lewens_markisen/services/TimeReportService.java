package de.lewens_markisen.services;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.Person;
import de.lewens_markisen.domain.TimeRegisterEvent;
import de.lewens_markisen.domain.TimeReport;
import de.lewens_markisen.repositories.TimeRegisterEventRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimeReportService {

	private final TimeRegisterEventRepository timeRegisterEventRepository;
	private final PersonService personService;

	public Optional<List<TimeRegisterEvent>> findPersonEvents(String bcCode) {
		Optional<Person> personOpt = personService.findByBcCode(bcCode);
		if (personOpt.isPresent()) {
			return Optional.of(timeRegisterEventRepository.findAllByPersonWithoutDubl(personOpt.get().getId()));

		} else {
			return Optional.empty();
		}
	}

	public Optional<TimeReport> createReport(String bcCode) {
		//@formatter:off
		Optional<Person> personOpt = personService.findByBcCode(bcCode);
		if (personOpt.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(TimeReport.builder()
					.person(personOpt.get())
					.timeRecords(timeRegisterEventRepository.findAllByPersonWithoutDubl(personOpt.get().getId()))
					.build());
		}
		//@formatter:on
	}

}
