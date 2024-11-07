package de.lewens_markisen.web.controllers.person;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;

import de.lewens_markisen.domain.local_db.Person;
import de.lewens_markisen.web.controllers.BaseIT;

@SpringBootTest
class PersonControllerEditTest extends BaseIT {

	public static final String API_EDIT = "/persons/edit/1";
	public static final String API_UPDATE = "/persons/update";

	@Test
	void editPersonNotAuth() throws Exception {
		mockMvc.perform(get(API_EDIT)).andExpect(status().is3xxRedirection());
		mockMvc.perform(get(API_UPDATE)).andExpect(status().is3xxRedirection());
	}

	@WithUserDetails("spring")
	@Rollback
	@Test
	void editPersonAuthAdmin() throws Exception {
		mockMvc.perform(get(API_EDIT)).andExpect(status().isOk()).andExpect(view().name("persons/personEdit"));
		
	    Person person = Person.builder().name("test person").nameForSearch("testperson").bcCode("2000").build();
		mockMvc.perform(post(API_UPDATE)
			.param("action", "update")
			.accept(MediaType.APPLICATION_JSON)
	        .characterEncoding("UTF-8")
	        .contentType(MediaType.APPLICATION_JSON)
	        .flashAttr("person", person))
	        .andExpect(status().is3xxRedirection())
	        .andExpect(view().name("redirect:/persons/list"));
	}
	
	@WithUserDetails("userPersonDepartment")
	@Test
	void editPersonUserAuthUserPersonDepartment() throws Exception {
		mockMvc.perform(get(API_EDIT)).andExpect(status().isForbidden());
		mockMvc.perform(post(API_UPDATE)).andExpect(status().is4xxClientError());
	}

	@WithUserDetails("user")
	@Test
	void editPersonUserAuthUser() throws Exception {
		mockMvc.perform(get(API_EDIT)).andExpect(status().isForbidden());
		mockMvc.perform(post(API_UPDATE)).andExpect(status().is4xxClientError());
	}

}
