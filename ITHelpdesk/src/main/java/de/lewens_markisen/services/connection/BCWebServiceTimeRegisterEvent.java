package de.lewens_markisen.services.connection;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.person.PersonPresence;
import de.lewens_markisen.domain.local_db.time_register_event.TimeRegisterEvent;
import de.lewens_markisen.person.PersonPresenceService;
import de.lewens_markisen.person.PersonService;
import de.lewens_markisen.services.connection.jsonModele.TimeRegisterEventJson;
import de.lewens_markisen.services.connection.jsonModele.TimeRegisterEventJsonList;
import de.lewens_markisen.timeReport.PeriodReport;
import de.lewens_markisen.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class BCWebServiceTimeRegisterEvent {
	private final ConnectionBC connectionBC;
	private final PersonService personService;
	private final PersonPresenceService personPresenceService;

	public Optional<List<TimeRegisterEvent>> readTimeRegisterEventsFromBC(Person person, PeriodReport period) {
		Optional<List<TimeRegisterEvent>> result = Optional.empty();
		try {
			log.debug("BC read from " + connectionBC.getWsZeitpunktposten());
			String requestZeitpunktposten = connectionBC.getUrl() + "/" + connectionBC.getWsZeitpunktposten()
					+ connectionBC.getFilter(createFilterPersonsTime(person.getBcCode(), period));
			Optional<String> anserOpt = connectionBC.createGETRequest(requestZeitpunktposten);
			if (anserOpt.isPresent()) {
				result = readTimeRegisterEventsFromJson(anserOpt.get());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private Optional<List<TimeRegisterEvent>> readTimeRegisterEventsFromJson(String anser) {
		Optional<List<TimeRegisterEvent>> result = Optional.empty();
		//@formatter:off
		try {
		    ObjectMapper objectMapper = JsonMapper.builder()
		    		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
		    		.build();
		    TimeRegisterEventJsonList l;
			l = objectMapper.readerFor(TimeRegisterEventJsonList.class)
					.readValue(anser);
			Optional<List<TimeRegisterEvent>> convertToTimeRegisterEvent = convertToTimeRegisterEvent(l.getValue());
			if (convertToTimeRegisterEvent.isPresent()) {
				return convertToTimeRegisterEvent;
			}
			else {
				return Optional.empty();
			}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	    //@formatter:on
		return result;
	}

	public Optional<List<TimeRegisterEvent>> convertToTimeRegisterEvent(List<TimeRegisterEventJson> value) {
		if (value.size() == 0) {
			return Optional.empty();
		}
		List<TimeRegisterEvent> events = new ArrayList<TimeRegisterEvent>();
		// @formatter:0ff
		Optional<Person> personOpt;
		for (TimeRegisterEventJson event: value) {
			personOpt = personService.findOrCreate(event.getPerson(), event.getName());
			if (personOpt.isPresent()) {
				events.add(TimeRegisterEvent.builder()
						.person(personOpt.get())
						.eventDate(event.getEventDate())
						.startTime(event.getStartDate())
						.endTime(event.getEndDate())
						.build());
			}
		}
		//@formatter:on
		return Optional.of(compoundDublRecords(events));
	}

	public List<TimeRegisterEvent> compoundDublRecords(List<TimeRegisterEvent> events) {
		List<TimeRegisterEvent> eventsCompound = new ArrayList<TimeRegisterEvent>();
		//@formatter:off
		// 1. Manual with Start and End Time
		events.stream()
			.filter(ev -> ev.getStartTime()!="" && ev.getEndTime()!="")
			.forEach(ev -> {
				eventsCompound.add(TimeRegisterEvent.builder()
					.person(ev.getPerson())
					.eventDate(ev.getEventDate())
					.startTime(ev.getStartTime())
					.endTime(ev.getEndTime())
					.build());
		});
		// 2. Auto separately Start and End time
		events.stream()
		.filter(ev -> ev.getStartTime()!="" && ev.getEndTime()=="")
		.forEach(ev -> {
			eventsCompound.add(TimeRegisterEvent.builder()
					.person(ev.getPerson())
					.eventDate(ev.getEventDate())
					.startTime(ev.getStartTime())
					.endTime(findEndTime(events, ev, ev.getStartTime()))
					.build());
		});
		//@formatter:on
		return eventsCompound;
	}

	private String findEndTime(List<TimeRegisterEvent> events, TimeRegisterEvent currentEvent, String startTime) {
		String result = "";
		for (TimeRegisterEvent ev : events) {
			//@formatter:off
			if (ev.getPerson().equals(currentEvent.getPerson()) 
					&& ev.getEventDate().equals(currentEvent.getEventDate())
					&& ev.getStartTime().equals("")
					&& TimeUtils.convertTimeToInt(ev.getEndTime()) > TimeUtils.convertTimeToInt(currentEvent.getStartTime()) ) {
				result = ev.getEndTime();
				break;
			}
			//@formatter:on
		}
		return result;
	}

	public void readPersonPresence() {
		personPresenceClear();
		personPresenceSave(getPersonArrival());
	}

	private Map<Person, String> getPersonArrival() {
		Map<Person, String> personArrival = new HashMap<Person, String>();
		
		Optional<List<TimeRegisterEvent>> eventsOpt = readTimeRegisterEventToday();
		if (eventsOpt.isEmpty()) {
			return personArrival;
		}
		List<TimeRegisterEvent> events = eventsOpt.get();
		events.stream().forEach(System.out::println);
		//@formatter:off
		return events.stream()
				.filter(e -> e.getEndTime().isBlank())
				.collect(Collectors.toMap(TimeRegisterEvent::getPerson, TimeRegisterEvent::getStartTime, (existing, replacement) -> existing));
		//@formatter:on
	}

	private void personPresenceSave(Map<Person, String> personArrival) {
		List<PersonPresence> presences = new ArrayList<PersonPresence>();
		PersonPresence personPresence;
	    for (Map.Entry<Person, String> entry : personArrival.entrySet()) {
			Optional<PersonPresence> personPresenceOpt = personPresenceService.findByPerson(entry.getKey());
			if (personPresenceOpt.isPresent()) {
				personPresence = personPresenceOpt.get();
				personPresence.setPresence(true);
				personPresence.setArrivalTime(entry.getValue());
				presences.add(personPresenceOpt.get());
			}
			else {
				//@formatter:off
				presences.add(PersonPresence.builder()
						.person(entry.getKey())
						.presence(true)
						.arrivalTime(entry.getValue())
						.build());
				//@formatter:on
			}
		}
		personPresenceService.saveAll(presences);
	}

	private void personPresenceClear() {
		List<PersonPresence> presences = personPresenceService.findAllByPresence(true);
		presences.stream().forEach(p -> p.setPresence(false));
		personPresenceService.saveAll(presences);
	}

	private Optional<List<TimeRegisterEvent>> readTimeRegisterEventToday() {
		Optional<List<TimeRegisterEvent>> result = Optional.empty();
		try {
			log.debug("BC read from " + connectionBC.getWsZeitpunktposten());
			String requestZeitpunktposten = connectionBC.getUrl() + "/" + connectionBC.getWsZeitpunktposten()
					+ connectionBC.getFilter(createFilterNow());
			Optional<String> anserOpt = connectionBC.createGETRequest(requestZeitpunktposten);
			if (anserOpt.isPresent()) {
				result = readTimeRegisterEventsFromJson(anserOpt.get());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private List<RestApiQueryFilter> createFilterPersonsTime(String bcCode, PeriodReport period) {
		List<RestApiQueryFilter> filter = new ArrayList<RestApiQueryFilter>();
		//@formatter:off
		filter.add(RestApiQueryFilter.builder()
				.attribute("Person")
				.comparisonType("eq")
				.value(bcCode)
				.stringAttribute(true)
				.build());
		filter.add(RestApiQueryFilter.builder()
				.attribute("Von_Datum")
				.comparisonType("ge")
				.value(period.getStart().toString())
				.stringAttribute(false)
				.build());
		filter.add(RestApiQueryFilter.builder()
				.attribute("Von_Datum")
				.comparisonType("le")
				.value(period.getEnd().toString())
				.stringAttribute(false)
				.build());
		//@formatter:on
		return filter;
	}

	private List<RestApiQueryFilter> createFilterNow() {
		LocalDate now = LocalDate.now();
		List<RestApiQueryFilter> filter = new ArrayList<RestApiQueryFilter>();
		//@formatter:off
		filter.add(RestApiQueryFilter.builder()
				.attribute("Von_Datum")
				.comparisonType("ge")
				.value(now.toString())
				.stringAttribute(false)
				.build());
		filter.add(RestApiQueryFilter.builder()
				.attribute("Von_Datum")
				.comparisonType("le")
				.value(now.toString())
				.stringAttribute(false)
				.build());
		//@formatter:on
		return filter;
	}

}
