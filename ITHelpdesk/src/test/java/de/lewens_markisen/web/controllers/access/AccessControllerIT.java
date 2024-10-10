package de.lewens_markisen.web.controllers.access;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import de.lewens_markisen.web.controllers.BaseIT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;

@SpringBootTest
public class AccessControllerIT extends BaseIT {

    public static final String API_LIST = "/accesses/list";
	public static final String VIEW = "access/accessList";

	@Test
	void listAccessesNotAuth() throws Exception {
		mockMvc.perform(get(API_LIST)).andExpect(status().is3xxRedirection());
	}

	@WithUserDetails("spring")
	@Test
	void listAccessesUserAuthAdmin() throws Exception {
		mockMvc.perform(get(API_LIST)).andExpect(status().isOk()).andExpect(view().name(VIEW));
	}

	@WithUserDetails("user")
	@Test
	void listAccessesUserAuthUser() throws Exception {
		mockMvc.perform(get(API_LIST)).andExpect(status().isForbidden());
	}

}