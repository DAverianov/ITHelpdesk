package de.lewens_markisen.web.controllers.email_account;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import de.lewens_markisen.domain.local_db.email.EmailAccountLss;
import de.lewens_markisen.email.EmailAccountService;
import de.lewens_markisen.repository.local.EmailAccountRepository;
import de.lewens_markisen.web.controllers.EmailAccountController;

@ExtendWith(MockitoExtension.class)
class EmailAccountControllerTest {
	
	private final Long ID = 1l;
	private final EmailAccountLss emailAccount = EmailAccountLss.builder().id(ID).build();

	public static final String API_LIST = "/email_accounts/list";
	public static final String API_EDIT = "/email_accounts/1";
	public static final String API_UPDATE = "/email_accounts/update";
	
	@Mock
	private EmailAccountRepository emailAccountRepository;
	@Mock
	private EmailAccountService emailAccountService;

	@InjectMocks
	private EmailAccountController controller;
	private List<EmailAccountLss> emailAccountList;

	private MockMvc mockMvc;
	private Page<EmailAccountLss> pagedResponse;

	@BeforeEach
	void setUp() {
		emailAccountList = new ArrayList<EmailAccountLss>();
		emailAccountList.add(EmailAccountLss.builder().build());
		pagedResponse = new PageImpl(emailAccountList);

		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@WithMockUser(roles = "ADMIN")
	@Test
	void list_whenCall_thenAnser() throws Exception {
		int pageSize = 12;
		Sort sort = Sort.by("email").ascending();
		Pageable pageable = PageRequest.of(0, pageSize, sort);
		
		Page<EmailAccountLss> page = new PageImpl<>(List.of(emailAccount), pageable, 0);		
		when(emailAccountService.findAll(pageable))
			.thenReturn(page);
		mockMvc.perform(get(API_LIST)).andExpect(status().isOk()).andExpect(view().name("email_accounts/emailAccountList"))
				.andExpect(model().attributeExists("accounts"));
		verifyNoInteractions(emailAccountRepository);
	}

	@Test
	@WithMockUser(roles = "USER")
	void showEditEmailAccountLssForm() throws Exception {
		//@formatter:off
		when(emailAccountService.findById(ID))
			.thenReturn(Optional.of(emailAccount));
		mockMvc.perform(get("/email_accounts/" + ID))
				.andExpect(status().isOk())
				.andExpect(view().name("email_accounts/emailAccountEdit"))
				.andExpect(model().attribute("account", hasProperty("id", is(ID))));
		//@formatter:on
		verifyNoInteractions(emailAccountRepository);
	}

//   @Test
	void processCreationForm() throws Exception {
		when(emailAccountRepository.save(ArgumentMatchers.any())).thenReturn(emailAccount);
		mockMvc.perform(post("/email_accounts/new")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/email_accounts/list"));
		verify(emailAccountRepository).save(ArgumentMatchers.any());
	}


}
