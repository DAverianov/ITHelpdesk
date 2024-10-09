package de.lewens_markisen.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import de.lewens_markisen.config.LewensportalAuthenticationProvider;

@ActiveProfiles("test")
@SpringBootTest()
//@Disabled
class LewensportalAuthenticationProviderTest {

	private final String TEST_USER = "test";
	private final String TEST_PASSWORD = "Fomalgaut1";
	
	@Autowired
	private LewensportalAuthenticationProvider lewensportalAuthenticationProvider;
	
	@Test
	public void authenticate_RealUserEmail() {
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(TEST_USER, TEST_PASSWORD);
		auth = (UsernamePasswordAuthenticationToken) lewensportalAuthenticationProvider.authenticate(auth);
	}

}
