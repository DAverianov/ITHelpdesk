package de.lewens_markisen.timeRegisterEvent;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.local_db.Person;
import de.lewens_markisen.domain.local_db.time_register_event.EventPersonMonth;
import de.lewens_markisen.domain.local_db.time_register_event.EventPersonMonthLoaded;
import de.lewens_markisen.repository.local.EventPersonMonthLoadedRepository;
import de.lewens_markisen.utils.DateUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EventPersonMonthLoadedServiceImpl implements EventPersonMonthLoadedService{
	private final EventPersonMonthLoadedRepository timeRegisterEventMonthLoadedRepository;
	
	@Override
	public Optional<EventPersonMonthLoaded> findByPersonAndEventAndMonth(Person person, EventPersonMonth event, LocalDate month) {
		return timeRegisterEventMonthLoadedRepository.findByPersonAndMonth(person, month);
	}

	@Override
	public EventPersonMonthLoaded save(Person person, EventPersonMonth event, LocalDate month) {
		LocalDate startOfMonth = DateUtils.startMonat(month);
		Optional<EventPersonMonthLoaded> recOpt = findByPersonAndEventAndMonth(person, event, startOfMonth);
		if (recOpt.isPresent()) {
			return recOpt.get();
		}
		else {
			//@formatter:off
			return timeRegisterEventMonthLoadedRepository.save(
					EventPersonMonthLoaded.builder()
					.event(event)
					.person(person)
					.month(DateUtils.startMonat(startOfMonth))
					.build());
			//@formatter:on
		}
	}

}
