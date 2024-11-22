package de.lewens_markisen.web.controllers.userspring;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;

import de.lewens_markisen.domain.local_db.security.UserSpring;
import de.lewens_markisen.security.RoleService;
import de.lewens_markisen.web.controllers.BaseIT;
import de.lewens_markisen.web.controllers.playlocad.UserRolesChecked;

@SpringBootTest
class UserSpringControllerEditTest extends BaseIT {

	@Autowired
	private RoleService roleService;
	
	public static final String API_EDIT = "/users/edit/1";
	public static final String API_UPDATE = "/users/update";

	@Test
	void listAccessesNotAuth() throws Exception {
		mockMvc.perform(get(API_EDIT)).andExpect(status().is3xxRedirection());
		mockMvc.perform(get(API_UPDATE)).andExpect(status().is3xxRedirection());
	}

	@WithUserDetails("spring")
	@Rollback
	@Test
	void editUserAuthAdmin() throws Exception {
		mockMvc.perform(get(API_EDIT)).andExpect(status().isOk());
		
		UserSpring user = UserSpring.builder().username("test user").build();
		mockMvc.perform(post(API_UPDATE)
			.param("action", "update")
			.accept(MediaType.APPLICATION_JSON)
	        .characterEncoding("UTF-8")
	        .contentType(MediaType.APPLICATION_JSON)
	        .flashAttr("user", user)
			.flashAttr("userRolesChecked", createUserRoles(user)))
	        .andExpect(status().is3xxRedirection());
	}

	private UserRolesChecked createUserRoles(UserSpring user) {
		UserRolesChecked userRoles = new UserRolesChecked();
		userRoles.setUser(user);
		userRoles.setAllRoles(roleService.findAll());
		userRoles.checkRoles();
		return userRoles;
	}
	
	@WithUserDetails("userPersonDepartment")
	@Test
	void editUserAuthUserPersonDepartment() throws Exception {
		mockMvc.perform(get(API_EDIT)).andExpect(status().isForbidden());
		mockMvc.perform(post(API_UPDATE)).andExpect(status().is4xxClientError());
	}

	@WithUserDetails("user")
	@Test
	void editUserAuthUser() throws Exception {
		mockMvc.perform(get(API_EDIT)).andExpect(status().isForbidden());
		mockMvc.perform(post(API_UPDATE)).andExpect(status().is4xxClientError());
	}

}
