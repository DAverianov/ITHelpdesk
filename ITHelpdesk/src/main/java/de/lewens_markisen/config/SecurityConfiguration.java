package de.lewens_markisen.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.security.Security;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices.RememberMeTokenAlgorithm;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Component
public class SecurityConfiguration {

	private final LewensportalAuthenticationProvider lewensportalAuthenticationProvider;
	private final PersistentTokenRepository persistentTokenRepository;
	private final UserDetailsService userDetailsService;
	private final String AD_DOMAINE;
	private final String AD_URL;
	private final String AD_ROOTDN;

	public SecurityConfiguration(LewensportalAuthenticationProvider lewensportalAuthenticationProvider,
			PersistentTokenRepository persistentTokenRepository, UserDetailsService userDetailsService,
			@Value("${spring.security.ad.domain}") String AD_DOMAINE, @Value("${spring.security.ad.url}") String AD_URL,
			@Value("${spring.security.ad.rootdn}") String AD_ROOTDN) {
		super();
		this.lewensportalAuthenticationProvider = lewensportalAuthenticationProvider;
		this.persistentTokenRepository = persistentTokenRepository;
		this.userDetailsService = userDetailsService;
		this.AD_DOMAINE = AD_DOMAINE;
		this.AD_URL = AD_URL;
		this.AD_ROOTDN = AD_ROOTDN;
		Security.addProvider(new BouncyCastleProvider());
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		//@formatter:off
		http
			.authorizeHttpRequests((authz) -> 
				authz
					.requestMatchers("/webjars/**", "/login", "/upload", "/resources/**", "/error").permitAll()
					.anyRequest().authenticated()
				)
            .formLogin(withDefaults())
            .rememberMe((remember) -> remember.tokenRepository(persistentTokenRepository)
                	.userDetailsService(userDetailsService))
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

	@Bean
	public AuthenticationManager authenticationManager() {
//		return new ProviderManager(Arrays.asList(activeDirectoryLdapAuthenticationProvider()));
		return new ProviderManager(Arrays.asList(lewensportalAuthenticationProvider));
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	// needed for use with Spring Data JPA SPeL
	@Bean
	public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
		return new SecurityEvaluationContextExtension();
	}

}
