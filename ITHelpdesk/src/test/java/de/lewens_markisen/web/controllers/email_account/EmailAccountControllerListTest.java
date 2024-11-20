package de.lewens_markisen.web.controllers.email_account;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import de.lewens_markisen.web.controllers.BaseIT;

@SpringBootTest
class EmailAccountControllerListTest extends BaseIT {
	
 	public static final String API_LIST = "/email_accounts/list";
	public static final String VIEW = "email_accounts/emailAccountList";

	@Test
	void listEmailAccountNotAuth() throws Exception {
		mockMvc.perform(get(API_LIST)).andExpect(status().is3xxRedirection());
	}

	@WithUserDetails("spring")
	@Test
	void listEmailAccountUserAuthAdmin() throws Exception {
		mockMvc.perform(get(API_LIST)).andExpect(status().isOk()).andExpect(view().name(VIEW));
	}
	
	@WithUserDetails("userPersonDepartment")
	@Test
	void listEmailAccountUserAuthUserPersonDepartment() throws Exception {
		mockMvc.perform(get(API_LIST)).andExpect(status().isForbidden());
	}

	@WithUserDetails("user")
	@Test
	void listEmailAccountUserAuthUser() throws Exception {
		mockMvc.perform(get(API_LIST)).andExpect(status().isForbidden());
	}

}
