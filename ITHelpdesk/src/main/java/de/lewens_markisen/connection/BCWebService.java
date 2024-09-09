package de.lewens_markisen.connection;

import java.util.List;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import de.lewens_markisen.domain.Person;
import de.lewens_markisen.domain.TimeRegisterEvent;
import de.lewens_markisen.domain.TimeRegisterEventList;

@Component
public class BCWebService {
	private final ConnectionBC connectionBC;

	public BCWebService(ConnectionBC connectionBC) {
		this.connectionBC = connectionBC;
	}

	public List<TimeRegisterEvent> readTimeRegisterEventsFromBC(Person person) {
		try {
			String requestZeitpunktposten = connectionBC.getUrl() + "/" + connectionBC.getWsZeitpunktposten()
					+ connectionBC.getFilter("Person", person.getBcCode());
			String anser = connectionBC.createGETRequest(requestZeitpunktposten);
			return readTimeRegisterEventsFromJson(anser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<TimeRegisterEvent> readTimeRegisterEventsFromJson(String anser) throws JsonMappingException, JsonProcessingException {
	    JsonNode rootNode;
	    //@formatter:off
	    ObjectMapper objectMapper = JsonMapper.builder()
	    		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
	    		.build();

	    //@formatter:on
	    TimeRegisterEventList l = objectMapper.readValue(anser, TimeRegisterEventList.class);
		return l.getValue();
	}


}
