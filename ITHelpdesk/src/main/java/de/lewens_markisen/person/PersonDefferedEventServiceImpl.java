package de.lewens_markisen.person;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.local_db.email.EmailLetter;
import de.lewens_markisen.domain.local_db.person.DefferedEvent;
import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.person.PersonDefferedEvent;
import de.lewens_markisen.domain.local_db.person.PersonPresence;
import de.lewens_markisen.domain.local_db.security.UserSpring;
import de.lewens_markisen.email.EmailLetterService;
import de.lewens_markisen.repository.local.PersonDefferedEventRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PersonDefferedEventServiceImpl
		implements
			PersonDefferedEventService {

	private final DefferedEvent EVENT_PERSON_KOMMEN = DefferedEvent.PERSON_KOMMEN;
	private final PersonService personService;
	private final PersonDefferedEventRepository personDefferedEventRepository;
	private final PersonPresenceService personPresenceService;
	private final EmailLetterService emailLetterService;

	@Override
	public List<PersonDefferedEvent> findAllByUserAndDefferedEvent(
			UserSpring user, DefferedEvent event) {
		return personDefferedEventRepository.findAllByUserAndDefferedEvent(user,
				event);
	}

	@Override
	public Boolean changeGlock(UserSpring userSpring, Long person_id) {
		Boolean result = false;
		Optional<Person> personOpt = personService.findById(person_id);
		if (personOpt.isPresent()) {
			List<PersonDefferedEvent> events = personDefferedEventRepository
					.findAllByUserAndPersonAndDefferedEvent(userSpring,
							personOpt.get(), EVENT_PERSON_KOMMEN);
			if (events.size() == 0) {
				createEvent(userSpring, personOpt.get(), EVENT_PERSON_KOMMEN);
				result = true;
			} else {
				changeEvents(events);
				result = true;
			}
		}
		return result;
	}

	private void changeEvents(List<PersonDefferedEvent> events) {
		// @formatter:off
		events.stream().forEach(e -> {
			if (e.getDone()) {
				e.setExecutionResult("closed manuelly");
			}
			e.setDone(!e.getDone());
		});
		personDefferedEventRepository.saveAll(events);
		// @formatter:on
	}

	private void createEvent(UserSpring userSpring, Person person,
			DefferedEvent eventType) {
		// @formatter:off
		personDefferedEventRepository.save(
				PersonDefferedEvent.builder()
					.user(userSpring)
					.person(person)
					.defferedEvent(eventType)
					.done(false)
					.build());
		// @formatter:on
	}

	@Override
	public void processEvents() {
		Map<Person, List<PersonDefferedEvent>> personsEvents = personDefferedEventRepository
				.findAllByDefferedEventAndDone(EVENT_PERSON_KOMMEN, false)
				.stream()
				.collect(Collectors.groupingBy(PersonDefferedEvent::getPerson));
		Map<Person, Boolean> personInPresence = personPresenceService
				.findAllByPresence(true)
				.stream()
				.collect(Collectors.toMap(PersonPresence::getPerson,
						PersonPresence::getPresence,
						(existing, replacement) -> existing));
		for (Entry<Person, List<PersonDefferedEvent>> entry : personsEvents
				.entrySet()) {
			if (personInPresence.containsKey(entry.getKey())) {
				for (PersonDefferedEvent personEvent: entry.getValue()) {
					createMassage(personEvent);
					doneEvent(personEvent);
				}
			}
		}
	}

	private void doneEvent(PersonDefferedEvent personEvent) {
		personEvent.setDone(true);
		personEvent.setExecutionResult("send email");
		personDefferedEventRepository.save(personEvent);
	}

	private void createMassage(PersonDefferedEvent personEvent) {
		emailLetterService.save(EmailLetter.builder()
				.sender(EmailLetter.DEFAULT_SENDER)
				.recipient(personEvent.getUser().getEmail())
				.subject("Erinnerung: "+personEvent.getPerson().getName()+" ist bereits im Büro.")
				.text("Automatisch generierte Email! Nicht antworten!")
				.dateToSenden(LocalDateTime.now())
				.boundaryDate(LocalDateTime.now().plus(3l, ChronoUnit.HOURS))
				.send(false)
				.build()
		);
		
	}

}
