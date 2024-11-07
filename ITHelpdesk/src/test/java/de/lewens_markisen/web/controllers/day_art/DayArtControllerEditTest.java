package de.lewens_markisen.web.controllers.day_art;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;

import de.lewens_markisen.domain.local_db.time_register_event.DayArt;
import de.lewens_markisen.timeRegisterEvent.DayArtService;
import de.lewens_markisen.web.controllers.BaseIT;

@SpringBootTest
public class DayArtControllerEditTest extends BaseIT {
	
	@Autowired
	private DayArtService dayArtService;

	public static final String NAME = "test";
	public static final String API_EDIT = "/day_arts/edit/1";
	public static final String API_UPDATE = "/day_arts/update";

	@Test
	void editDayArtNotAuth() throws Exception {
		mockMvc.perform(get(API_EDIT)).andExpect(status().is3xxRedirection());
		mockMvc.perform(get(API_UPDATE)).andExpect(status().is3xxRedirection());
	}

	@WithUserDetails("spring")
	@Rollback
	@Test
	void editDayArtAuthAdmin() throws Exception {
		mockMvc.perform(get(API_EDIT)).andExpect(status().isOk()).andExpect(view().name("day_arts/dayArtEdit"));
		
		mockMvc.perform(post(API_UPDATE)
			.param("action", "update")
			.accept(MediaType.APPLICATION_JSON)
	        .characterEncoding("UTF-8")
	        .contentType(MediaType.APPLICATION_JSON)
	        .flashAttr("dayArt", createDayArt()))
	        .andExpect(status().is3xxRedirection())
	        .andExpect(view().name("redirect:/day_arts/list"));
	}
	
	private DayArt createDayArt() {
	    Optional<DayArt> dayArtOpt = dayArtService.findByName(NAME);
	    DayArt dayArt;
	    if (dayArtOpt.isEmpty()) {
	    	dayArt = dayArtService.save(DayArt.builder().name("test").minuten(45).build());
	    }
	    else {
	    	dayArt = dayArtOpt.get();
	    }
	    return dayArt;
	}

	@WithUserDetails("userPersonDepartment")
	@Test
	void eeditDayArtAuthUserPersonDepartment() throws Exception {
		mockMvc.perform(get(API_EDIT)).andExpect(status().isOk());
		mockMvc.perform(post(API_UPDATE)
				.param("action", "update")
				.accept(MediaType.APPLICATION_JSON)
		        .characterEncoding("UTF-8")
		        .contentType(MediaType.APPLICATION_JSON)
		        .flashAttr("dayArt", createDayArt()))
		        .andExpect(status().is3xxRedirection())
		        .andExpect(view().name("redirect:/day_arts/list"));
	}

	@WithUserDetails("user")
	@Test
	void editDayArtAuthUser() throws Exception {
		mockMvc.perform(get(API_EDIT)).andExpect(status().isForbidden());
		mockMvc.perform(post(API_UPDATE)).andExpect(status().is4xxClientError());
	}


}
