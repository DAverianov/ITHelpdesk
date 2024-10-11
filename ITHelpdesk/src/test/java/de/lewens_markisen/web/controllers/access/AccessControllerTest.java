package de.lewens_markisen.web.controllers.access;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import de.lewens_markisen.access.AccessService;
import de.lewens_markisen.domain.localDb.Access;
import de.lewens_markisen.repository.local.AccessRepository;
import de.lewens_markisen.web.controllers.AccessController;

@ExtendWith(MockitoExtension.class)
class AccessControllerTest {
	@Mock
	private AccessRepository accessRepository;
	@Mock
	private AccessService accessService;

	@InjectMocks
	private AccessController controller;
	private List<Access> accessList;
	private final Long ID = 1l;
	private Access access;

	private MockMvc mockMvc;
	private Page<Access> accesses;
	private Page<Access> pagedResponse;

	@BeforeEach
	void setUp() {
		accessList = new ArrayList<Access>();
		accessList.add(Access.builder().build());
		pagedResponse = new PageImpl(accessList);

		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	void initCreationForm() throws Exception {
		mockMvc.perform(get("/accesses/new")).andExpect(status().isOk()).andExpect(view().name("access/createAccess"))
				.andExpect(model().attributeExists("access"));
		verifyNoInteractions(accessRepository);
	}

	@WithMockUser(roles = "ADMIN")
	@Test
	@Disabled
	void list_whenCall_thenAnser() throws Exception {
		mockMvc.perform(get("/accesses/list")).andExpect(status().isOk()).andExpect(view().name("access/accessList"))
				.andExpect(model().attributeExists("accesses"));
		verifyNoInteractions(accessRepository);
	}

	// ToDO: Mocking Page
	void processFindFormReturnMany() throws Exception {
		when(accessRepository.findAllByName(anyString(), PageRequest.of(0, 10, Sort.Direction.DESC, "name")))
				.thenReturn(pagedResponse);
		mockMvc.perform(get("/accesses")).andExpect(status().isOk()).andExpect(view().name("access/accessList"))
				.andExpect(model().attribute("selections", hasSize(2)));
	}

	@Test
	@WithMockUser(roles = "USER")
	void showEditAccessForm() throws Exception {
		//@formatter:off
		when(accessService.findById(ID))
			.thenReturn(Optional.of(Access.builder().id(ID).build()));
		mockMvc.perform(get("/accesses/" + ID))
				.andExpect(status().isOk())
				.andExpect(view().name("access/accessEdit"))
				.andExpect(model().attribute("access", hasProperty("id", is(ID))));
		//@formatter:on
		verifyNoInteractions(accessRepository);
	}

//   @Test
	void processCreationForm() throws Exception {
		when(accessRepository.save(ArgumentMatchers.any())).thenReturn(Access.builder().id(ID).build());
		mockMvc.perform(post("/accesses/new")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/accesses/list"));
		verify(accessRepository).save(ArgumentMatchers.any());
	}

}
