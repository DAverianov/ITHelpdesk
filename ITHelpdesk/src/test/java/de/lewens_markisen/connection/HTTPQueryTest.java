package de.lewens_markisen.connection;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import de.lewens_markisen.domain.Person;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(classes = { ConnectionBC.class })
@AutoConfigurationPackage
@SpringBootConfiguration
class HTTPQueryTest {

	@Autowired
	private ConnectionBC connectionBC;

	@Test
	public void getHTML_whenHook_thenBekommeAnser() {
		Person person = Person.builder().name("user").bcCode("643").build();
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
}
