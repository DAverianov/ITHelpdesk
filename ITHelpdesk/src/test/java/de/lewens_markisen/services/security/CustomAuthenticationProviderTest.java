package de.lewens_markisen.services.security;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest()
@Disabled
class CustomAuthenticationProviderTest {
	@Autowired
	CustomAuthenticationProvider provider;
	
	@Test
	void authenticate_RealUserEmail() {
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("it-team", "1#01");
		auth = (UsernamePasswordAuthenticationToken) provider.authenticate(auth);
	}

}
