package de.lewens_markisen.services.connection;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import de.lewens_markisen.domain.Person;

@ActiveProfiles("test")
@SpringBootTest()
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
			assertNotNull(anser);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
