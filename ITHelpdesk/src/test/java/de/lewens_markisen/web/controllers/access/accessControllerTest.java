package de.lewens_markisen.web.controllers.access;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
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
import java.util.UUID;

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
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import de.lewens_markisen.access.Access;
import de.lewens_markisen.access.AccessController;
import de.lewens_markisen.repository.AccessRepository;

@ExtendWith(MockitoExtension.class)
class accessControllerTest {
	@Mock
	AccessRepository accessRepository;

    @InjectMocks
    AccessController controller;
    List<Access> accessList;
    Long id;
    Access access;

    MockMvc mockMvc;
    Page<Access> accesses;
    Page<Access> pagedResponse;

    @BeforeEach
    void setUp() {
    	accessList = new ArrayList<Access>();
    	accessList.add(Access.builder().build());
    	accessList.add(Access.builder().build());
        pagedResponse = new PageImpl(accessList);

        final Long id = 100l;

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void initCreationForm() throws Exception {
        mockMvc.perform(get("/accesses/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("access/createAccess"))
                .andExpect(model().attributeExists("access"));
        verifyNoInteractions(accessRepository);
    }
    @Test
    void list_whenCall_thenAnser() throws Exception{
        mockMvc.perform(get("/accesses/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("access/accessList"))
                .andExpect(model().attributeExists("accesses"));
        verifyNoInteractions(accessRepository);
    }

    //ToDO: Mocking Page
   void processFindFormReturnMany() throws Exception{
       when(accessRepository.findAllByName(anyString(), PageRequest.of(0,
             10,Sort.Direction.DESC,"name"))).thenReturn(pagedResponse);
       mockMvc.perform(get("/accesses"))
               .andExpect(status().isOk())
               .andExpect(view().name("access/accessList"))
               .andExpect(model().attribute("selections", hasSize(2)));
   }
   @Test
   void showEditAccessForm() throws Exception{
       when(accessRepository.findById(id)).thenReturn(Optional.of(Access.builder().id(id).build()));
       mockMvc.perform(get("/accesses/"+id))
               .andExpect(status().isOk())
               .andExpect(view().name("accesses/accessEdit"))
               .andExpect(model().attribute("access", hasProperty("id", is(id))));
   }

   @Test
   void processCreationForm() throws Exception {
       when(accessRepository.save(ArgumentMatchers.any())).thenReturn(Access.builder().id(id).build());
       mockMvc.perform(post("/accesses/new"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/accesses/list"));
       verify(accessRepository).save(ArgumentMatchers.any());
   }

 
}
