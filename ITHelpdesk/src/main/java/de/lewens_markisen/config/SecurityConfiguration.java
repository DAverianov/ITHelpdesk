package de.lewens_markisen.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

import de.lewens_markisen.domain.security.Authority;
import de.lewens_markisen.domain.security.UserSpring;
import de.lewens_markisen.services.security.UserService;
import jakarta.transaction.Transactional;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration implements AuthenticationProvider {

	private final UserService userService;
	private final String AD_DOMAINE;
	private final String AD_URL;
	private final String AD_ROOTDN;

	public SecurityConfiguration(@Autowired UserService userService,
			@Value("${spring.security.ad.domain}") String AD_DOMAINE, 
			@Value("${spring.security.ad.url}") String AD_URL,
			@Value("${spring.security.ad.rootdn}") String AD_ROOTDN) {
		super();
		this.userService = userService;
		this.AD_DOMAINE = AD_DOMAINE;
		this.AD_URL = AD_URL;
		this.AD_ROOTDN = AD_ROOTDN;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		//@formatter:off
		http
			.authorizeHttpRequests((authz) -> 
				authz
					.requestMatchers("/webjars/**", "/login", "/logout", "/resources/**", "/error").permitAll()
					.requestMatchers("/", "/timeReport/me").hasAnyRole("USER")
					.requestMatchers("/persons/**").hasAnyRole("ADMIN")
					.requestMatchers("/accesses/**").hasAnyRole("ADMIN")
					.anyRequest().authenticated()
				)
            .formLogin(withDefaults());
			
		//@formatter:on
		return http.build();
	}

	public AuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
		ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(AD_DOMAINE,
				AD_URL, AD_ROOTDN);
		// VornameName without space
		provider.setSearchFilter("(&(objectClass=user)(sAMAccountName={1}))");
		provider.setConvertSubErrorCodesToExceptions(true);
		provider.setUseAuthenticationRequestCredentials(true);
		provider.setAuthoritiesMapper(userAuthoritiesMapper());
		return provider;
	}

	@Bean
	public GrantedAuthoritiesMapper userAuthoritiesMapper() {
		return (authorities) -> {
			Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
			mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			return mappedAuthorities;
		};
	}

	@Override
	@Transactional
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Authentication auth = activeDirectoryLdapAuthenticationProvider().authenticate(authentication);

		String username = auth.getName();
		Optional<UserSpring> userOpt = userService.getUserByName(username);
		UserSpring user;
		if (userOpt.isPresent()) {
			user = userOpt.get();
			UserDetails principal = User.builder().username(user.getUsername()).password("")
					.authorities(convertToSpringAuthorities(user.getAuthorities())).build();
			return new UsernamePasswordAuthenticationToken(principal, principal.getPassword(),
					principal.getAuthorities());

		} else {
			user = userService.createUser(auth.getName(), "");
		}
		return auth;
	}

	private Collection<? extends GrantedAuthority> convertToSpringAuthorities(Set<Authority> authorities) {
		if (authorities != null && authorities.size() > 0) {
			return authorities.stream().map(Authority::getRole).map(SimpleGrantedAuthority::new)
					.collect(Collectors.toSet());
		} else {
			return new HashSet<>();
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(Arrays.asList(activeDirectoryLdapAuthenticationProvider()));
	}

}
