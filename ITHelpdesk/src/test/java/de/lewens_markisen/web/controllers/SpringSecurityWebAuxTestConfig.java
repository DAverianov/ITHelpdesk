package de.lewens_markisen.web.controllers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.security.Role;
import de.lewens_markisen.domain.local_db.security.UserSpring;
import de.lewens_markisen.repository.local.security.RoleRepository;
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
		
		Set<Role> personDepartmentRoles = new HashSet<Role>();
		personDepartmentRoles.add(userRole);
		personDepartmentRoles.add(personDepartmentRole);
		
		Person person = Person.builder().id(1l).name("Test").build();

		//@formatter:off
		UserSpring userAdmin = UserSpring.builder()
				.username("spring")
				.password("guru")
				.person(person)
				.role(adminRole).build();

		UserSpring userUser = UserSpring.builder()
				.username("user")
				.password("pass")
				.person(person)
				.role(userRole).build();

		UserSpring userPersonalAbteilung = UserSpring.builder()
				.username("userPersonDepartment")
				.password("pass")
				.person(person)
				.roles(personDepartmentRoles)
				.build();
				
		//@formatter:on

		return new InMemoryUserDetailsManager(Arrays.asList(userAdmin, userUser, userPersonalAbteilung));
	}
}
