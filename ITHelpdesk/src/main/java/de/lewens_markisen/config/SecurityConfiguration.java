package de.lewens_markisen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;
import java.util.Arrays;

import org.springframework.security.authentication.ProviderManager;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Bean
	public ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
		ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider("LSS.local",
				"ldap://LSS-DC-2019.LSS.local", "ou=mybusiness,dc=lss,dc=local");
		// VornameName without space
		provider.setSearchFilter("(&(objectClass=user)(sAMAccountName={1}))");
		provider.setConvertSubErrorCodesToExceptions(true);
		provider.setUseAuthenticationRequestCredentials(true);
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(Arrays.asList(activeDirectoryLdapAuthenticationProvider()));
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authz) -> authz.requestMatchers("/", "/webjars/**", "/login", "/resources/**")
				.permitAll().anyRequest().authenticated()).httpBasic(withDefaults());
		return http.build();
	}

}
