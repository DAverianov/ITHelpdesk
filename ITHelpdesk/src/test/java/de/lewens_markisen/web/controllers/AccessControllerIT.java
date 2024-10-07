package de.lewens_markisen.web.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import de.lewens_markisen.repository.AccessRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(SpringSecurityWebAuxTestConfig.class)
public class AccessControllerIT extends BaseIT{

    @Autowired
    AccessRepository accessRepository;
   
    @WithUserDetails("spring")
    @Test
    void listAccessesUserAuthAdmin() throws Exception {
 
        mockMvc.perform(get("/accesses/list"))
		        .andExpect(status().isOk())
		        .andExpect(view().name("access/accessList"));
    }

    @DisplayName("test secure /access/list")
    @Nested
    class InitNewForm{

//        @ParameterizedTest(name = "#{index} with [{arguments}]")
//        @MethodSource("de.lewens_markisen.web.controllers.AccessControllerIT#getStreamAllUsers")
        @Test
    	void initCreationFormAuth() throws Exception {

        	mockMvc.perform(get("/accesses/list")
            		.with(httpBasic("spring", "guru")))
                    .andExpect(status().isOk())
                    .andExpect(view().name("access/accessList"));
        }

        @Test
        void initCreationFormNotAuth() throws Exception {
            mockMvc.perform(get("/accesses/list"))
                    .andExpect(status().is3xxRedirection());
        }
    }

    @DisplayName("List Accesses")
    @Nested
    class ListAccess{
        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("de.lewens_markisen.web.controllers.AccessControllerIT#getStreamAdminUser")
//    	@WithUserDetails("spring")
//    	@Test
        void testListCustomersAUTH(String user, String pwd) throws Exception {
            mockMvc.perform(get("/accesses/list")
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());

        }

        @Test
        void testListCustomersNOTAUTH() throws Exception {
            mockMvc.perform(get("/accesses/list")
                    .with(httpBasic("user", "password")))
            		.andExpect(status().is3xxRedirection());
        }

        @Test
        void testListCustomersNOTLOGGEDIN() throws Exception {
            mockMvc.perform(get("/accesses/list"))
                    .andExpect(status().is3xxRedirection());

        }
    }

}