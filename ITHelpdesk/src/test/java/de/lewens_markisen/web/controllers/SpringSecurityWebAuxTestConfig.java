package de.lewens_markisen.web.controllers;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import de.lewens_markisen.domain.security.Role;
import de.lewens_markisen.domain.security.UserSpring;
import de.lewens_markisen.repository.security.RoleRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@TestConfiguration
public class SpringSecurityWebAuxTestConfig {
	private final RoleRepository roleRepository;

	@Bean
	@Primary
	public UserDetailsService userDetailsService() {

		Role adminRole = roleRepository.findByName("ADMIN").get();
		Role userRole = roleRepository.findByName("USER").get();
		Role personDepartmentRole = roleRepository.findByName("PERSON_DEPARTMENT").get();
		System.out.println("  userDetailsService");

		//@formatter:off
		UserSpring userAdmin = UserSpring.builder()
				.username("spring")
				.password("guru")
				.role(adminRole).build();

		UserSpring userUser = UserSpring.builder()
				.username("user")
				.password("pass")
				.role(userRole).build();

		UserSpring userPersonalAbteilung = UserSpring.builder()
				.username("userPersonDepartment")
				.password("pass")
				.role(personDepartmentRole).build();
				
		//@formatter:on

		return new InMemoryUserDetailsManager(Arrays.asList(userAdmin, userUser, userPersonalAbteilung));
	}
}
