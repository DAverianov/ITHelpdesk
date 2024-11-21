package de.lewens_markisen.person;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.local_db.person.DefferedEvent;
import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.person.PersonDefferedEvent;
import de.lewens_markisen.domain.local_db.security.UserSpring;
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

	@Override
	public List<PersonDefferedEvent> findAllByUserAndDefferedEvent(UserSpring user, DefferedEvent event) {
		return personDefferedEventRepository.findAllByUserAndDefferedEvent(user, event);
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
				result = false;
			}
		}
		return result;
	}

	private void changeEvents(List<PersonDefferedEvent> events) {
		//@formatter:off
		events.stream().forEach(e -> {
			if (e.getDone()) {
				e.setExecutionResult("closed manuelly");
			}
			e.setDone(!e.getDone());
			}
		);
		personDefferedEventRepository.saveAll(events);
		//@formatter:on
	}

	private void createEvent(UserSpring userSpring, Person person,
			DefferedEvent eventType) {
		//@formatter:off
		personDefferedEventRepository.save(
				PersonDefferedEvent.builder()
				.user(userSpring)
				.person(person)
				.defferedEvent(eventType)
				.done(false)
				.build());
		//@formatter:on
	}

}
