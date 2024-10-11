package de.lewens_markisen.config;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

@ActiveProfiles("test")
@SpringBootTest()
//@Disabled
@WebAppConfiguration
class SecurityConfigurationTest {

	private final String USER = "test";
	private final String PASSWORD = "Fomalgaut1";


	@Test
	@WithMockUser(username = "test", password = "Fomalgaut1")
    public void authenticate_Test() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();

        assertThat(loggedUsername).isEqualTo("test");
	}
}
