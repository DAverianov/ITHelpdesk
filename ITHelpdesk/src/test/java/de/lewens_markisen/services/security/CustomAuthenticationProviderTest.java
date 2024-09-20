package de.lewens_markisen.services.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest()
class CustomAuthenticationProviderTest {
	@Autowired
	CustomAuthenticationProvider provider;
	
	@Test
	void authenticate_RealUserEmail() {
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("it-team", "1#01");
		auth = (UsernamePasswordAuthenticationToken) provider.authenticate(auth);
	}

}
