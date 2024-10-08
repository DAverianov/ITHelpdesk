package de.lewens_markisen.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import de.lewens_markisen.domain.localDb.security.UserSpring;
import de.lewens_markisen.security.UserSpringService;
import de.lewens_markisen.security.UserSpringServiceImpl;
import jakarta.transaction.Transactional;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Component
public class SecurityConfiguration implements AuthenticationProvider {

	private final UserSpringService userService;
	private final String AD_DOMAINE;
	private final String AD_URL;
	private final String AD_ROOTDN;

	public SecurityConfiguration(@Autowired UserSpringServiceImpl userService,
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
					.requestMatchers("/webjars/**", "/login", "/resources/**", "/error").permitAll()
					.anyRequest().authenticated()
				)
            .formLogin(withDefaults())
            .csrf(csrf -> csrf.disable());

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

		String userName = userService.convertNameToLowCase(auth.getName());
		Optional<UserSpring> userOpt = userService.getUserByName(userName);
		UserSpring user;
		if (userOpt.isPresent()) {
			user = userOpt.get();
		} else {
			user = userService.createUser(userName, "");
		}
		return new UsernamePasswordAuthenticationToken(user, user.getPassword(),
				user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(Arrays.asList(activeDirectoryLdapAuthenticationProvider()));
	}
	
	@Bean
	public PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}

}
