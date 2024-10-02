package de.lewens_markisen.web.controllers;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import de.lewens_markisen.domain.security.Authority;
import de.lewens_markisen.repository.security.AuthorityRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
@TestConfiguration
public class SpringSecurityWebAuxTestConfig {
	private final AuthorityRepository authorityRepository;

	@Bean
	@Primary
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		//@formatter:off
		http
			.authorizeHttpRequests((authz) -> 
				authz
					.requestMatchers("/webjars/**", "/login", "/resources/**", "/error").permitAll()
					.anyRequest().authenticated()
				)
           .httpBasic(withDefaults());
			
		//@formatter:on
		return http.build();
	}

	@Bean
	@Primary
	public UserDetailsService userDetailsService() {

		Authority adminRole = authorityRepository.findByRole("ROLE_ADMIN").get();
		Authority userRole = authorityRepository.findByRole("ROLE_USER").get();
		Authority userPersonalAbteilungRole = authorityRepository.findByRole("ROLE_PERSONALABTEILUNG").get();

		Set<Authority> auth = new HashSet<Authority>();
		auth.add(userRole);
		auth.add(adminRole);
		//@formatter:off
		UserDetails userAdmin = new org.springframework.security.core.userdetails.User(
				"spring", 
				"guru", true, true,	true, true, 
				convertToSpringAuthorities(auth));

		auth = new HashSet<Authority>();
		auth.add(userRole);
		UserDetails userUser = new org.springframework.security.core.userdetails.User(
				"user", 
				"pass", true, true, true, true, 
				convertToSpringAuthorities(auth));

		auth = new HashSet<Authority>();
		auth.add(userRole);
		auth.add(userPersonalAbteilungRole);
		UserDetails userPersonalAbteilung = new org.springframework.security.core.userdetails.User(
				"userPersonalAbteilung", 
				"pass", true, true, true, true, 
				convertToSpringAuthorities(auth));
		//@formatter:on

		return new InMemoryUserDetailsManager(Arrays.asList(userAdmin, userUser, userPersonalAbteilung));
	}

	private Collection<? extends GrantedAuthority> convertToSpringAuthorities(Set<Authority> authorities) {
		if (authorities != null && authorities.size() > 0) {
			return authorities.stream().map(Authority::getRole).map(SimpleGrantedAuthority::new)
					.collect(Collectors.toSet());
		} else {
			return new HashSet<>();
		}
	}
}
