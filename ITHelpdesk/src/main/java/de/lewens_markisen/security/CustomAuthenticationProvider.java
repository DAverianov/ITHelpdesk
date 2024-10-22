package de.lewens_markisen.security;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.stereotype.Component;

import de.lewens_markisen.domain.local_db.security.UserSpring;
import lombok.AllArgsConstructor;

@AllArgsConstructor
//@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	private final AuthenticationProvider authenticationProvider;
	private final UserSpringServiceImpl userService;

	public AuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
		ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider("LSS.local",
				"ldap://LSS-DC-2019.LSS.local", "ou=mybusiness,dc=lss,dc=local");
		// VornameName without space
		provider.setSearchFilter("(&(objectClass=user)(sAMAccountName={1}))");
		provider.setConvertSubErrorCodesToExceptions(true);
		provider.setUseAuthenticationRequestCredentials(true);
		provider.setAuthoritiesMapper(userAuthoritiesMapper());
		return provider;
	}

	public GrantedAuthoritiesMapper userAuthoritiesMapper() {
		return (authorities) -> {
			Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
			mappedAuthorities.add(new SimpleGrantedAuthority("USER"));
			System.out.println("..auth"+authorities);
			return mappedAuthorities;
		};
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		// check whether user exist in myuser table
		Optional<UserSpring> userOpt = userService.getUserByName(username);
		Authentication auth = activeDirectoryLdapAuthenticationProvider().authenticate(authentication);
     	UserSpring user;
		if (userOpt.isPresent()) {
	     	user = userOpt.get();
		} else {
    		user = userService.createUser(auth.getName(), "");
		}
		return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
