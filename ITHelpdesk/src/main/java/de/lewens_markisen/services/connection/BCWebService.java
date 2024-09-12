package de.lewens_markisen.services.connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import de.lewens_markisen.domain.Person;
import de.lewens_markisen.domain.TimeRegisterEvent;
import de.lewens_markisen.services.PersonService;
import de.lewens_markisen.services.connection.jsonModele.TimeRegisterEventJson;
import de.lewens_markisen.services.connection.jsonModele.TimeRegisterEventJsonList;

@Component
public class BCWebService {
	private final ConnectionBC connectionBC;
	private final PersonService personService;

	public BCWebService(ConnectionBC connectionBC, PersonService personService) {
		this.connectionBC = connectionBC;
		this.personService = personService;
	}

	public Optional<List<TimeRegisterEvent>> readTimeRegisterEventsFromBC(Person person) {
		Optional<List<TimeRegisterEvent>> result = Optional.empty();
		try {
			String requestZeitpunktposten = connectionBC.getUrl() + "/" + connectionBC.getWsZeitpunktposten()
					+ connectionBC.getFilter(createFilter(person.getBcCode()));
			Optional<String> anserOpt = connectionBC.createGETRequest(requestZeitpunktposten);
			if (anserOpt.isPresent()) {
				result = readTimeRegisterEventsFromJson(anserOpt.get());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private List<RestApiQueryFilter> createFilter(String bcCode) {
		List<RestApiQueryFilter> filter = new ArrayList<RestApiQueryFilter>();
		//@formatter:off
		filter.add(RestApiQueryFilter.builder()
				.attribute("Von_Datum")
				.comparisonType("ge")
				.value("2024-09-01")
				.stringAttribute(false)
				.build());
		filter.add(RestApiQueryFilter.builder()
				.attribute("Person")
				.comparisonType("eq")
				.value(bcCode)
				.stringAttribute(true)
				.build());
		//@formatter:on
		return filter;
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

	private Optional<List<TimeRegisterEvent>> convertToTimeRegisterEvent(List<TimeRegisterEventJson> value) {
		if (value.size()==0) {
			return Optional.empty();
		}
		Optional<Person> personOpt = personService.findOrCreate(value.get(0).getPerson(), value.get(0).getName());
		if (personOpt.isEmpty()) {
			return Optional.empty();
		}
		List<TimeRegisterEvent> events = new ArrayList<TimeRegisterEvent>();
		//@formatter:0ff
		value.stream().forEach(tR -> events.add(
				TimeRegisterEvent.builder()
					.person(personOpt.get())
					.eventDate(tR.getEventDate())
					.startDate(tR.getStartDate())
					.endDate(tR.getEndDate())
					.build()));
		//@formatter:on
		return Optional.of(events);
	}

	public List<String> createTimeReport(Person person) {
		Optional<List<TimeRegisterEvent>> eventsOpt = readTimeRegisterEventsFromBC(person);
		if (eventsOpt.isPresent()) {
			return formattTimeEventsToString(eventsOpt.get());
		}
		else {
			return List.of("Es gibt keine Daten für Person "+person.toString()+"!");
		}
	}

	private List<String> formattTimeEventsToString(List<TimeRegisterEvent> events) {
		List<String> res = new ArrayList<String>();
		events.stream().forEach(ev -> res.add(ev.toString()));
		return res;
	}

}
