package de.lewens_markisen.web.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@Import(SpringSecurityWebAuxTestConfig.class)
public abstract class BaseIT {
	@Autowired
	WebApplicationContext wac;

	protected MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
	}

	public static Stream<Arguments> getStreamAdminUser() {
		return Stream.of(Arguments.of("spring", "guru"));
	}

	public static Stream<Arguments> getStreamAllUsers() {
		return Stream.of(Arguments.of("spring", "guru"), 
				Arguments.of("user", "pass"),
				Arguments.of("userPersonalAbteilung", "pass"));
	}

	public static Stream<Arguments> getStreamNotAdmin() {
		return Stream.of(Arguments.of("user", "pass"), 
				Arguments.of("userPersonalAbteilung", "pass"));
	}
}
