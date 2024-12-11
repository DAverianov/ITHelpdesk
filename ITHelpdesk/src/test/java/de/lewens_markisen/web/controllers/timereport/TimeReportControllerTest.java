package de.lewens_markisen.web.controllers.timereport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;

import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.time_register_event.TimeRegisterEvent;
import de.lewens_markisen.repository.local.TimeRegisterEventRepository;
import de.lewens_markisen.utils.FileUtilsLss;
import de.lewens_markisen.web.controllers.BaseIT;
import kong.unirest.Body;
import kong.unirest.Unirest;

@SpringBootTest
class TimeReportControllerTest extends BaseIT{

	private static final String API_TIMEREPORT = "/timeReport";
	private static final String ME = "/me";
	private static final String BC_CODE = "/1071";
	
	@Mock
	private TimeRegisterEventRepository timeRegisterEventRepository;

	@BeforeEach
	public void setupTimeRegisterEventRepository() {
		Person person = Person.builder().name("Test").build();
		String startTime = "04.10.2024";
        List<TimeRegisterEvent> timeRecords = new ArrayList<TimeRegisterEvent>();
        //@formatter:off
        timeRecords.add(
        		TimeRegisterEvent.builder()
        		.person(person)
        		.startTime(startTime)
        		.endTime(startTime)
        		.build());
        //@formatter:on
        
		when(timeRegisterEventRepository.findAllByPerson(person)).thenReturn(timeRecords);
	}
	
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
		mockMvc.perform(get(API_TIMEREPORT+BC_CODE)).andExpect(status().isOk()).andExpect(view().name("timeReport/timeReportPeriod"));
	}
	
	@WithUserDetails("userPersonDepartment")
	@Test
	void timeReport_AuthUserPersonDepartment() throws Exception {
		mockMvc.perform(get(API_TIMEREPORT+ME)).andExpect(status().isOk());
		mockMvc.perform(get(API_TIMEREPORT+BC_CODE)).andExpect(status().isOk()).andExpect(view().name("timeReport/timeReportPeriod"));
	}
	
	@WithUserDetails("userPersonDepartment")
	@Test
	void timeReportPDF_AuthUserPersonDepartment() throws Exception {
		//@formatter:off
		mockMvc.perform(post(API_TIMEREPORT+"/pdf")
				.param("action", "update")
				.accept(MediaType.APPLICATION_JSON)
		        .characterEncoding("UTF-8")
		        .contentType(MediaType.APPLICATION_PDF)
		        .flashAttr("bcCode", BC_CODE));
		//@formatter:on
	}

	@WithUserDetails("user")
	@Test
	void timeReport_AuthUser() throws Exception {
		mockMvc.perform(get(API_TIMEREPORT+ME)).andExpect(status().isOk());
		mockMvc.perform(get(API_TIMEREPORT+BC_CODE)).andExpect(status().isForbidden());
	}

}
