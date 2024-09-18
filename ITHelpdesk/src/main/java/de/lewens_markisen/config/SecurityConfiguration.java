package de.lewens_markisen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.ldap.EmbeddedLdapServerContextSourceFactoryBean;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.PersonContextMapper;
import org.springframework.security.web.SecurityFilterChain;

import de.lewens_markisen.security.ExampleAuthenticationSuccessEventListener;
import de.lewens_markisen.services.security.UserService;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

import org.springframework.security.authentication.ProviderManager;
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

//	@Bean
//	public ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
//		ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(
//				"LSS.local", "ldap://192.168.0.19");
//		provider.setConvertSubErrorCodesToExceptions(true);
//		provider.setUseAuthenticationRequestCredentials(true);
//		provider.setConvertSubErrorCodesToExceptions(true);
//		return provider;
//	}

//	@Bean
//	public AuthenticationManager authenticationManager() {
//		return new ProviderManager(Arrays.asList(activeDirectoryLdapAuthenticationProvider()));
//	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http.authorizeHttpRequests((authz) -> authz.requestMatchers("/", "/webjars/**", "/login", "/resources/**")
//				.permitAll().anyRequest().authenticated()).httpBasic(withDefaults());
		return http.build();
	}

}
