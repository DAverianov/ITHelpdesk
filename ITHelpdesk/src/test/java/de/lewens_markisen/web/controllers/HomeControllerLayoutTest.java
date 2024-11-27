package de.lewens_markisen.web.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
class HomeControllerLayoutTest extends BaseIT {

	public static final String API = "/home";
	public static final String VIEW = "home";

	@Test
	void layoutMenuNotAuth() throws Exception {
		MvcResult result = mockMvc.perform(get(API)).andExpect(status().is3xxRedirection()).andReturn();
	    String content = result.getResponse().getContentAsString();
	    assertNotNull(content);
	    assertFalse(content.contains("Home"));
	    assertFalse(content.contains("Anweisungen"));
	    assertFalse(content.contains("Persons"));
	    assertFalse(content.contains("Zeitbericht"));
	    assertFalse(content.contains("Accesses"));
	    assertFalse(content.contains("Users"));
	    assertFalse(content.contains("Logs"));
	    assertFalse(content.contains("Exit"));
	}

	@WithUserDetails("spring")
	@Test
	void layoutMenuUserAuthAdmin() throws Exception {
		MvcResult result =	mockMvc.perform(get(API)).andExpect(status().isOk()).andExpect(view().name(VIEW)).andReturn();
	    String content = result.getResponse().getContentAsString();
	    assertNotNull(content);
	    assertTrue(content.contains("Home"));
	    assertTrue(content.contains("Anweisungen"));
	    assertTrue(content.contains("Persons"));
	    assertTrue(content.contains("Zeitbericht"));
	    assertTrue(content.contains("Accesses"));
	    assertTrue(content.contains("Users"));
	    assertTrue(content.contains("Logs"));
	    assertTrue(content.contains("Exit"));
	}

	@WithUserDetails("userPersonDepartment")
	@Test
	void layoutMenuAuthUserPersonDepartment() throws Exception {
		MvcResult result =	mockMvc.perform(get(API)).andExpect(status().isOk()).andExpect(view().name(VIEW)).andReturn();
	    String content = result.getResponse().getContentAsString();
	    assertNotNull(content);
	    assertTrue(content.contains("Home"));
	    assertFalse(content.contains("Anweisungen"));
	    assertTrue(content.contains("Persons"));
	    assertTrue(content.contains("Zeitbericht"));
	    assertFalse(content.contains("Accesses"));
	    assertFalse(content.contains("Users"));
	    assertFalse(content.contains("Logs"));
	    assertTrue(content.contains("Exit"));
	}

	@WithUserDetails("user")
	@Test
	void layoutMenuAuthUser() throws Exception {
		MvcResult result =	mockMvc.perform(get(API)).andExpect(status().isOk()).andReturn();
	    String content = result.getResponse().getContentAsString();
	    assertNotNull(content);
	    assertTrue(content.contains("Home"));
	    assertFalse(content.contains("Anweisungen"));
	    assertFalse(content.contains("Persons"));
	    assertTrue(content.contains("Zeitbericht"));
	    assertFalse(content.contains("Accesses"));
	    assertFalse(content.contains("Users"));
	    assertFalse(content.contains("Logs"));
	    assertTrue(content.contains("Exit"));
	}

}
