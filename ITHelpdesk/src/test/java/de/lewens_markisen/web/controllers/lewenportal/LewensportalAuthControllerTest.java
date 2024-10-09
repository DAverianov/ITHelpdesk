package de.lewens_markisen.web.controllers.lewenportal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;

import de.lewens_markisen.web.controllers.BaseIT;
import de.lewens_markisen.web.controllers.playlocad.AuthPayload;

@SpringBootTest
class LewensportalAuthControllerTest extends BaseIT{

	public static final String API = "/lewensportal/auth";

	@Test
	void lewensportalAuth_NotAuth() throws Exception {
		mockMvc.perform(get(API)).andExpect(status().is3xxRedirection());
		mockMvc.perform(post(API)).andExpect(status().is3xxRedirection());
	}

	@WithUserDetails("spring")
	@Rollback
	@Test
	void lewensportalAuth_AuthAdmin() throws Exception {
		mockMvc.perform(get(API)).andExpect(status().isOk());
		
		AuthPayload payload = new AuthPayload("test", "Fomalgaut1");
		mockMvc.perform(post(API)
			.param("action", "update")
			.accept(MediaType.APPLICATION_JSON)
	        .characterEncoding("UTF-8")
	        .contentType(MediaType.APPLICATION_JSON)
	        .flashAttr("payload", payload))
	        .andExpect(status().isOk())
	        .andExpect(view().name("lewensportal/auth"));
	}
}
