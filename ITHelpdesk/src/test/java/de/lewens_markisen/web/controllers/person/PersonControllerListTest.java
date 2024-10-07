package de.lewens_markisen.web.controllers.person;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import de.lewens_markisen.web.controllers.BaseIT;

@SpringBootTest
class PersonControllerListTest extends BaseIT {

	public static final String API_LIST = "/persons/list";

	@Test
	void listAccessesNotAuth() throws Exception {
		mockMvc.perform(get(API_LIST)).andExpect(status().is3xxRedirection());
	}

	@WithUserDetails("spring")
	@Test
	void listAccessesUserAuthAdmin() throws Exception {
		mockMvc.perform(get(API_LIST)).andExpect(status().isOk()).andExpect(view().name("persons/personsList"));
	}
	
	@WithUserDetails("userPersonDepartment")
	@Test
	void listAccessesUserAuthUserPersonDepartment() throws Exception {
		mockMvc.perform(get(API_LIST)).andExpect(status().isOk()).andExpect(view().name("persons/personsList"));
	}

	@WithUserDetails("user")
	@Test
	void listAccessesUserAuthUser() throws Exception {
		mockMvc.perform(get(API_LIST)).andExpect(status().isForbidden());
	}

}
