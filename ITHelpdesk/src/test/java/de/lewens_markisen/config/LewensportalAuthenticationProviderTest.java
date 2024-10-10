package de.lewens_markisen.config;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@SpringBootTest
class LewensportalAuthenticationProviderTest {

	@Autowired
	private LewensportalAuthenticationProvider provider;

	private final String USER = "test";
	private final String PASSWORD = "Fomalgaut1";

	@Test
	void authenticate_whenUser_thenReturnUserWithAuthorities() {
		UsernamePasswordAuthenticationToken authIn = new UsernamePasswordAuthenticationToken(USER, PASSWORD);
		UsernamePasswordAuthenticationToken authOut = (UsernamePasswordAuthenticationToken) provider
				.authenticate(authIn);
		String loggedUsername = authOut.getName();

		assertThat(loggedUsername).isEqualTo(USER.toLowerCase());
	}

}
