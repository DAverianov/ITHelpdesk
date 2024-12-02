package de.lewens_markisen.timeRegisterEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.time_register_event.EventPersonMonth;
import de.lewens_markisen.domain.local_db.time_register_event.TimeRegisterEvent;
import de.lewens_markisen.repository.local.TimeRegisterEventRepository;
import de.lewens_markisen.services.connection.BCWebServiceTimeRegisterEvent;
import de.lewens_markisen.timeReport.PeriodReport;
import de.lewens_markisen.utils.DateUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TimeRegisterEventServiceImpl implements TimeRegisterEventService {

	private final TimeRegisterEventRepository timeRegisterEventRepository;
	private final EventPersonMonthLoadedService timeRegisterEventMonthLoadedService;
	private final BCWebServiceTimeRegisterEvent bcWebService;

	@Override
	@Transactional
	public void readEventsProPerson(Person person, PeriodReport period) {
		if (allRecordsSavedInLocalDB(person, period)) {
			return;
		} else {
			LocalDate month = period.getStart();
			while (month.isBefore(period.getEnd())) {
				deleteRecordsInMonth(person, month);
				month = month.plusMonths(1);
			}
			readFromBCAndSave(person, period);
			registerSavedPeriodInLocalDB(person, period);
		}
	}

	private void readFromBCAndSave(Person person, PeriodReport period) {
		// read from BC
		Optional<List<TimeRegisterEvent>> eventsBC = bcWebService
				.readTimeRegisterEventsFromBC(person, period);
		// save
		if (eventsBC.isPresent()) {
			eventsBC.get().stream().forEach(
					e -> e.setMonth(DateUtils.startMonat(e.getEventDate())));
			timeRegisterEventRepository.saveAll(eventsBC.get());
		}
	}

	private void deleteRecordsInMonth(Person person, LocalDate month) {
		List<TimeRegisterEvent> events = timeRegisterEventRepository
				.findAllByPersonAndMonth(person, month);
		timeRegisterEventRepository.deleteAll(events);
	}

	private boolean allRecordsSavedInLocalDB(Person person,
			PeriodReport period) {
		LocalDate d = period.getStart();
		while (d.isBefore(period.getEnd())) {
			if (!allRecordsSavedInLocalDB(person, period.getStart())) {
				return false;
			}
			d = d.plusMonths(1);
		}
		return true;
	}

	private boolean allRecordsSavedInLocalDB(Person person, LocalDate start) {
		return timeRegisterEventMonthLoadedService.findByPersonAndEventAndMonth(
				person, EventPersonMonth.TIMEREGISTEREVENT,
				DateUtils.startMonat(start)).isPresent();
	}

	private void registerSavedPeriodInLocalDB(Person person,
			PeriodReport period) {
		LocalDate d = DateUtils.startMonat(period.getStart());
		LocalDate thisMonth = DateUtils.startMonat(LocalDate.now());
		while (d.isBefore(period.getEnd()) && d.isBefore(thisMonth)) {
			timeRegisterEventMonthLoadedService.save(person,
					EventPersonMonth.TIMEREGISTEREVENT,
					DateUtils.startMonat(d));
			d = d.plusMonths(1);
		}
	}

	@Override
	public Optional<List<TimeRegisterEvent>> findAllByPerson(Person person,
			PeriodReport period) {
		return Optional.of(timeRegisterEventRepository.findAllByPerson(person));
	}

	@Override
	public List<TimeRegisterEvent> findAllByPersonAndMonth(Person person,
			LocalDate month) {
		List<TimeRegisterEvent> events = timeRegisterEventRepository
				.findAllByPersonAndMonth(person, month);
		return addEmptyDate(events, person, month);
	}

	private List<TimeRegisterEvent> addEmptyDate(List<TimeRegisterEvent> events,
			Person person, LocalDate month) {
		List<LocalDate> dates = DateUtils.getDateInMonth(month);
		for (LocalDate d : dates) {
			// @formatter:off
			if (events.stream().filter(e -> e.getEventDate().equals(d))
					.count() == 0) {
				events.add(TimeRegisterEvent.builder().person(person)
						.month(month).eventDate(d).startTime("").endTime("")
						.build());
			}
			// @formatter:on
		}
		return events;
	}

}
