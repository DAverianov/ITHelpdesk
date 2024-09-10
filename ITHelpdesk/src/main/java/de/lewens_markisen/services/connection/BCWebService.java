package de.lewens_markisen.services.connection;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import de.lewens_markisen.domain.BaseEntity;
import de.lewens_markisen.domain.Person;
import de.lewens_markisen.domain.TimeRegisterEvent;
import de.lewens_markisen.services.PersonService;
import de.lewens_markisen.services.connection.jsonModele.TimeRegisterEventJson;
import de.lewens_markisen.services.connection.jsonModele.TimeRegisterEventJsonList;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
					+ connectionBC.getFilter("Person", person.getBcCode());
			String anser = connectionBC.createGETRequest(requestZeitpunktposten);
			result = readTimeRegisterEventsFromJson(anser);
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

	private Optional<List<TimeRegisterEvent>> convertToTimeRegisterEvent(List<TimeRegisterEventJson> value) {
		if (value.size()==0) {
			return Optional.empty();
		}
		Optional<Person> personOpt = personService.getPersonOrCreate(value.get(0).getPerson(), value.get(0).getName());
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

}
