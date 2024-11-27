package de.lewens_markisen.web.controllers.person;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;

import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.web.controllers.BaseIT;

@SpringBootTest
class PersonControllerDeleteTest extends BaseIT {

	public static final String API_DELETE = "/persons/delete";

	@Test
	void deletePersonNotAuth() throws Exception {
		mockMvc.perform(post(API_DELETE)).andExpect(status().is3xxRedirection());
	}

	@WithUserDetails("spring")
	@Rollback
	@Test
	void deletePersonAuthAdmin() throws Exception {
	    Person person = Person.builder().name("test person").nameForSearch("testperson").bcCode("2000").build();
		mockMvc.perform(post(API_DELETE)
			.accept(MediaType.APPLICATION_JSON)
	        .characterEncoding("UTF-8")
	        .contentType(MediaType.APPLICATION_JSON)
	        .flashAttr("person", person))
	        .andExpect(status().is3xxRedirection())
	        .andExpect(view().name("redirect:/persons/list"));
	}
	
	@WithUserDetails("userPersonDepartment")
	@Test
	void deletePersonAuthUserPersonDepartment() throws Exception {
		mockMvc.perform(post(API_DELETE)).andExpect(status().is4xxClientError());
	}

	@WithUserDetails("user")
	@Test
	void deletePersonAuthUser() throws Exception {
		mockMvc.perform(post(API_DELETE)).andExpect(status().is4xxClientError());
	}

}
