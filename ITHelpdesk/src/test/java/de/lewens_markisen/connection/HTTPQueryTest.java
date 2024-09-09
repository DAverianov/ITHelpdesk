package de.lewens_markisen.connection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import de.lewens_markisen.domain.Person;
import de.lewens_markisen.domain.TimeRegisterEvent;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(classes = { ConnectionBC.class, BCWebService.class })
@AutoConfigurationPackage
@SpringBootConfiguration
class HTTPQueryTest {
	private final String BC_CODE = "645";

	@Autowired
	private ConnectionBC connectionBC;
	@Autowired
	private BCWebService BCWebService;

	@Test
	public void getHTML_whenHook_thenBekommeAnser() {
		Person person = Person.builder().name("user").bcCode(BC_CODE).build();
		try {
			String requestZeitpunktposten = connectionBC.getUrl() + "/" + connectionBC.getWsZeitpunktposten()
					+ connectionBC.getFilter("Person", person.getBcCode());
			String anser = connectionBC.createGETRequest(requestZeitpunktposten);
			JSONObject o = new JSONObject(anser);
			assertNotNull(anser);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void getHTML_whenHook_thenReceiveValidJSON() {
		Person person = Person.builder().name("user").bcCode(BC_CODE).build();
		try {
			List<TimeRegisterEvent> events = BCWebService.readTimeRegisterEventsFromBC(person);
			assertThat(events).isNotEmpty().hasAtLeastOneElementOfType(TimeRegisterEvent.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
