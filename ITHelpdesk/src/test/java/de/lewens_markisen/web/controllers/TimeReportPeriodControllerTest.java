package de.lewens_markisen.web.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class TimeReportPeriodControllerTest extends BaseIT{

	public static final String API_TIMEREPORT = "/timeReportPeriod";
	public static final String ME = "/me";

	@WithUserDetails("spring")
	@Rollback
	@Test
	void timeReport_AuthAdmin() throws Exception {
		mockMvc.perform(get(API_TIMEREPORT+ME)).andExpect(status().isOk());
	}
}
