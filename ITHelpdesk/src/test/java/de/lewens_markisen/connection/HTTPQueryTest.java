package de.lewens_markisen.connection;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import de.lewens_markisen.domain.Person;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(classes = {ConnectionBC.class})
@AutoConfigurationPackage
@SpringBootConfiguration
//@Sql(scripts = "classpath:insert-data.sql")
class HTTPQueryTest {
	
	@Autowired
	ConnectionBC connectionBC;
	
	@Value("${businesscentral.url}")
	private String url;
	@Value("${businesscentral.ws.zeitpunktposten}")
	private String wsZeitpunktposten;

	@Test
	void getHTML_whenHook_thenBekommeAnser() {
		Person person = new Person("user", "1071");
		HTTPQuery query = new HTTPQuery();
		try {
			String anser = query.getHTML(url + "/" + wsZeitpunktposten+connectionBC.getFilter("Person", person.getBcCode()));
			assertNotNull(anser);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
