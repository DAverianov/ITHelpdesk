package de.lewens_markisen.timeRegisterEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.localDb.Person;
import de.lewens_markisen.domain.localDb.TimeRegisterEvent;
import de.lewens_markisen.repository.local.TimeRegisterEventRepository;
import de.lewens_markisen.services.connection.BCWebService;
import de.lewens_markisen.timeReport.PeriodReport;
import jakarta.transaction.Transactional;

@Service
public class TimeRegisterEventServiceImpl implements TimeRegisterEventService {

	private final TimeRegisterEventRepository timeRegisterEventRepository;
	private final BCWebService bcWebService;

	public TimeRegisterEventServiceImpl(TimeRegisterEventRepository timeRegisterEventRepository, BCWebService bcWebService) {
		this.timeRegisterEventRepository = timeRegisterEventRepository;
		this.bcWebService = bcWebService;
	}

	@Override
	@Transactional
	public Optional<List<TimeRegisterEvent>> readEventsProPerson(Person person, PeriodReport period) {
		// delete all records
		List<TimeRegisterEvent> events = timeRegisterEventRepository.findAllByPerson(person);
		timeRegisterEventRepository.deleteAll(events);
		// read from BC
		Optional<List<TimeRegisterEvent>> eventsBC = bcWebService.readTimeRegisterEventsFromBC(person, period);
		List<TimeRegisterEvent> eventsBCsaved = new ArrayList<TimeRegisterEvent>();
		// save
		if (eventsBC.isPresent()) {
			timeRegisterEventRepository.saveAll(eventsBC.get());
			return Optional.of(eventsBCsaved);
		}
		else {
			return Optional.empty();
		}
	}

	@Override
	public Optional<List<TimeRegisterEvent>> findAllByPerson(Person person, PeriodReport period) {
		return Optional.of(timeRegisterEventRepository.findAllByPerson(person));
	}

}
