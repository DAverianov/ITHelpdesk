package de.lewens_markisen.services.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.stereotype.Component;

import de.lewens_markisen.domain.security.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
//@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	private final AuthenticationProvider authenticationProvider;
	private final UserService userService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		// check whether user exist in myuser table
		Optional<User> userOpt = userService.getUserByName(username);

		if (userOpt.isPresent()) {
			return authenticationProvider.authenticate(authentication);
		} else {
			throw new UsernameNotFoundException("User not found.");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
