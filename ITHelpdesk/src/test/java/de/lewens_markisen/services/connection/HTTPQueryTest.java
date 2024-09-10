package de.lewens_markisen.services.connection;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest()
class HTTPQueryTest {

	@Autowired
	private ConnectionBC connectionBC;

	@Test
	public void getHTML_whenHook_thenBekommeAnser() {
		try {
			String requestZeitpunktposten = connectionBC.getUrl() + "/" + connectionBC.getWsZeitpunktposten()
					+ "?$filter=Person%20eq%20%27645%27";
			String anser = connectionBC.createGETRequest(requestZeitpunktposten);
			assertNotNull(anser);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
