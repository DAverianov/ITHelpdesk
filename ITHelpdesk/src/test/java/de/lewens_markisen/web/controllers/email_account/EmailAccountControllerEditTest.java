package de.lewens_markisen.web.controllers.email_account;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import de.lewens_markisen.domain.local_db.email.EmailAccountLss;
import de.lewens_markisen.email.EmailAccountService;
import de.lewens_markisen.web.controllers.BaseIT;

@SpringBootTest
class EmailAccountControllerEditTest extends BaseIT {

	public static final String API_EDIT = "/email_accounts/1";
	public static final String API_UPDATE = "/email_accounts/update";

	@Autowired
	private EmailAccountService emailAccountService;
	private EmailAccountLss account;

	@BeforeEach
	void setUp() {
		account = emailAccountService.findById(1l).get();
	}
	
	@Test
	void editEmailAccountNotAuth() throws Exception {
		mockMvc.perform(get(API_EDIT)).andExpect(status().is3xxRedirection());
		mockMvc.perform(get(API_UPDATE)).andExpect(status().is3xxRedirection());
	}

	@WithUserDetails("spring")
	@Rollback
	@Test
	void editEmailAccountAuthAdmin() throws Exception {
		mockMvc.perform(get(API_EDIT)).andExpect(status().isOk()).andExpect(view().name("email_accounts/emailAccountEdit"));
		
	    Map<String, Object> attr = new HashMap<String, Object>();
	    attr.put("account", account);
	    attr.put("accessName", "BC develop");
	    
		mockMvc.perform(post(API_UPDATE)
			.param("action", "update")
			.accept(MediaType.APPLICATION_JSON)
	        .characterEncoding("UTF-8")
	        .contentType(MediaType.APPLICATION_JSON)
	        .flashAttrs(attr))
	        .andExpect(status().is3xxRedirection())
	        .andExpect(view().name("redirect:/email_accounts/list"));
	}
	
	@WithUserDetails("userPersonDepartment")
	@Test
	void editEmailAccountUserAuthUserPersonDepartment() throws Exception {
		mockMvc.perform(get(API_EDIT)).andExpect(status().isForbidden());
		mockMvc.perform(post(API_UPDATE)).andExpect(status().is4xxClientError());
	}

	@WithUserDetails("user")
	@Test
	void editEmailAccountUserAuthUser() throws Exception {
		mockMvc.perform(get(API_EDIT)).andExpect(status().isForbidden());
		mockMvc.perform(post(API_UPDATE)).andExpect(status().is4xxClientError());
	}

}
