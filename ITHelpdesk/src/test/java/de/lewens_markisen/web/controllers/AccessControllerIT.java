package de.lewens_markisen.web.controllers;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.lewens_markisen.repository.AccessRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class AccessControllerIT extends BaseIT{

    @Autowired
    AccessRepository accessRepository;

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

//	@DisplayName("Init Find Beer Form")
//	@Nested
//	class FindForm {
//		@ParameterizedTest(name = "#{index} with [{arguments}]")
//		@MethodSource("guru.sfg.brewery.web.controllers.AccessControllerIT#getStreamAllUsers")
//		void findBeersFormAUTH(String user, String pwd) throws Exception {
//			mockMvc.perform(get("/beers/find").with(httpBasic(user, pwd))).andExpect(status().isOk())
//					.andExpect(view().name("beers/findBeers")).andExpect(model().attributeExists("beer"));
//		}
//
//		@Test
//		void findBeersWithAnonymous() throws Exception {
//			mockMvc.perform(get("/beers/find").with(anonymous())).andExpect(status().isUnauthorized());
//		}
//	}
//
//	@DisplayName("Process Find Beer Form")
//	@Nested
//	class ProcessFindForm {
//		@Test
//		void findBeerForm() throws Exception {
//			mockMvc.perform(get("/beers").param("beerName", "")).andExpect(status().isUnauthorized());
//		}
//
//		@ParameterizedTest(name = "#{index} with [{arguments}]")
//		@MethodSource("guru.sfg.brewery.web.controllers.AccessControllerIT#getStreamAllUsers")
//		void findBeerFormAuth(String user, String pwd) throws Exception {
//			mockMvc.perform(get("/beers").param("beerName", "").with(httpBasic(user, pwd))).andExpect(status().isOk());
//		}
//	}
//
//	@DisplayName("Get Beer By Id")
//	@Nested
//	class GetByID {
//		@ParameterizedTest(name = "#{index} with [{arguments}]")
//		@MethodSource("guru.sfg.brewery.web.controllers.AccessControllerIT#getStreamAllUsers")
//		void getBeerByIdAUTH(String user, String pwd) throws Exception {
//			Beer beer = beerRepository.findAll().get(0);
//
//			mockMvc.perform(get("/beers/" + beer.getId()).with(httpBasic(user, pwd))).andExpect(status().isOk())
//					.andExpect(view().name("beers/beerDetails")).andExpect(model().attributeExists("beer"));
//		}
//
//		@Test
//		void getBeerByIdNoAuth() throws Exception {
//			Beer beer = beerRepository.findAll().get(0);
//
//			mockMvc.perform(get("/beers/" + beer.getId())).andExpect(status().isUnauthorized());
//		}
//    }
}