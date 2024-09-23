package de.lewens_markisen.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import de.lewens_markisen.services.security.CustomAuthenticationProvider;

@ActiveProfiles("test")
@SpringBootTest()
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
