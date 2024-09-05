package de.lewens_markisen.connection;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import de.lewens_markisen.timeRecordingSystems.domain.Person;

@SpringBootTest
class HTTPQueryTest {
	@Autowired
	ConnectionWeb connectionBC;
	@Value("${businesscentral.ws.zeitpunktposten}")
	private String wsZeitpunktposten;

	@Test
	void getHTML_whenHook_thenBekommeAnser() {
		Person person = new Person(null, "1071", "user");
		HTTPQuery query = new HTTPQuery();
		try {
			String anser = query.getHTML(connectionBC.getUrl() + "/" + wsZeitpunktposten+connectionBC.getFilter("Person", person.getBcCode()));
			assertNotNull(anser);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
