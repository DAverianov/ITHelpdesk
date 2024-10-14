package de.lewens_markisen.security;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;

import de.lewens_markisen.web.controllers.SpringSecurityWebAuxTestConfig;

@Import(SpringSecurityWebAuxTestConfig.class)
@SpringBootTest
class UserSpringServiceImplTest {

	@Autowired
	private UserSpringServiceImpl userSpringService;
	
	@WithUserDetails("spring")
	@Test
	void getCurrentUser_whenFind_thanBekomme() {
		String username = userSpringService.getAuthenticationName();
		assertThat(username).isNotNull();
	}

}
