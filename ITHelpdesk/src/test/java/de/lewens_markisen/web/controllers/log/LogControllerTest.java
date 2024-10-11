package de.lewens_markisen.web.controllers.log;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import de.lewens_markisen.web.controllers.BaseIT;

@SpringBootTest
class LogControllerTest extends BaseIT {

	public static final String API_LIST = "/logs/list";
	public static final String VIEW = "logs/logsList";

	@Test
	void listPersonNotAuth() throws Exception {
		mockMvc.perform(get(API_LIST)).andExpect(status().is3xxRedirection());
	}

	@WithUserDetails("spring")
	@Test
	void listPersonUserAuthAdmin() throws Exception {
		mockMvc.perform(get(API_LIST)).andExpect(status().isOk()).andExpect(view().name(VIEW));
	}
	
	@WithUserDetails("userPersonDepartment")
	@Test
	void listPersonUserAuthUserPersonDepartment() throws Exception {
		mockMvc.perform(get(API_LIST)).andExpect(status().isForbidden());
	}

	@WithUserDetails("user")
	@Test
	void listPersonUserAuthUser() throws Exception {
		mockMvc.perform(get(API_LIST)).andExpect(status().isForbidden());
	}

}
