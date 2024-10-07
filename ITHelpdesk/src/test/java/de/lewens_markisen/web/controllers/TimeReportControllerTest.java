package de.lewens_markisen.web.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class TimeReportControllerTest extends BaseIT{

	public static final String API_TIMEREPORT = "/timeReport";
	public static final String ME = "/me";
	public static final String BC_CODE = "/1071";

	@Test
	void timeReport_NotAuth() throws Exception {
		mockMvc.perform(get(API_TIMEREPORT+ME)).andExpect(status().is3xxRedirection());
		mockMvc.perform(get(API_TIMEREPORT+BC_CODE)).andExpect(status().is3xxRedirection());
	}

	@WithUserDetails("spring")
	@Rollback
	@Test
	void timeReport_AuthAdmin() throws Exception {
		mockMvc.perform(get(API_TIMEREPORT+ME)).andExpect(status().isOk());
		mockMvc.perform(get(API_TIMEREPORT+BC_CODE)).andExpect(status().isOk()).andExpect(view().name("timeReport/timeReport"));
	}
	
	@WithUserDetails("userPersonDepartment")
	@Test
	void timeReport_AuthUserPersonDepartment() throws Exception {
		mockMvc.perform(get(API_TIMEREPORT+ME)).andExpect(status().isOk());
		mockMvc.perform(get(API_TIMEREPORT+BC_CODE)).andExpect(status().isOk()).andExpect(view().name("timeReport/timeReport"));
	}

	@WithUserDetails("user")
	@Test
	void timeReport_AuthUser() throws Exception {
		mockMvc.perform(get(API_TIMEREPORT+ME)).andExpect(status().isOk());
		mockMvc.perform(get(API_TIMEREPORT+BC_CODE)).andExpect(status().isForbidden());
	}

}
