package de.lewens_markisen.web.controllers.userspring;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;

import de.lewens_markisen.domain.local_db.security.UserSpring;
import de.lewens_markisen.web.controllers.BaseIT;

@SpringBootTest
class UserSpringControllerDeleteTest extends BaseIT {

	public static final String API_DELETE = "/users/delete";

	@Test
	void deleteUserNotAuth() throws Exception {
		mockMvc.perform(post(API_DELETE)).andExpect(status().is3xxRedirection());
	}

	@WithUserDetails("spring")
	@Rollback
	@Test
	void deleteUserAuthAdmin() throws Exception {
		UserSpring user = UserSpring.builder().username("test user").build();
		//@formatter:off
		mockMvc.perform(post(API_DELETE)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.contentType(MediaType.APPLICATION_JSON)
				.flashAttr("user", user))
				.andExpect(status().is4xxClientError());
		//@formatter:on
	}

	@WithUserDetails("userPersonDepartment")
	@Test
	void deleteUserAuthUserPersonDepartment() throws Exception {
		mockMvc.perform(post(API_DELETE)).andExpect(status().is4xxClientError());
	}

	@WithUserDetails("user")
	@Test
	void deleteUserAuthUser() throws Exception {
		mockMvc.perform(post(API_DELETE)).andExpect(status().is4xxClientError());
	}

}
