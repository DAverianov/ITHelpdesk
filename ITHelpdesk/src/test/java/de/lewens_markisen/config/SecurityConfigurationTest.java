package de.lewens_markisen.config;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

@ActiveProfiles("test")
@SpringBootTest()
@Disabled
@WebAppConfiguration
class SecurityConfigurationTest {
	@Autowired
	SecurityConfiguration provider;

	@Test
	void authenticate() {
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("it-team", "*#01");
		auth = (UsernamePasswordAuthenticationToken) provider.authenticate(auth);
	}

}
