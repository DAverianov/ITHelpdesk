package de.lewens_markisen.config;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticationProvider;
import org.springframework.stereotype.Component;

import de.lewens_markisen.security.LssUser;
import de.lewens_markisen.security.LssUserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class LewensportalAuthenticationProvider extends AbstractLdapAuthenticationProvider {

	private final LssUserService lssUserService;
	
	@Override
	protected DirContextOperations doAuthentication(UsernamePasswordAuthenticationToken auth) {
		String username = auth.getName();
		String password = (String) auth.getCredentials();
		DirContext ctx = null;
		try {
			return searchForUser(username, password);
		} catch (NamingException ex) {
			this.logger.error("Failed to locate directory entry for authenticated user: " + username, ex);
			throw badCredentials(ex);
		} finally {
			LdapUtils.closeContext(ctx);
		}
	}


	private DirContextOperations searchForUser(String username, String password) throws NamingException {

		Optional<LssUser> lssUserOpt;
		try {
			lssUserOpt = lssUserService.findUserByName(username);
		}
		catch (Exception e) {
			throw new UsernameNotFoundException("User " + username + " not found in lewensportal DB!");
		}
		if (lssUserOpt.isEmpty()) {
			throw new UsernameNotFoundException("User " + username + " not found in lewensportal DB!");
		}
		try {
			if (lssUserService.getLssHashPassword(password, lssUserOpt.get().getSalt()).equals(lssUserOpt.get().getPassword())) {
				return new DirContextAdapter();
			}
			else {
				UsernameNotFoundException userNameNotFoundException = new UsernameNotFoundException(
						"User " + username + " not correct password!");
				throw badCredentials(userNameNotFoundException);
				
			}
		} catch (NoSuchAlgorithmException e) {
			UsernameNotFoundException userNameNotFoundException = new UsernameNotFoundException(
					"NoSuchAlgorithmException", e);
			throw badCredentials(userNameNotFoundException);
		}
	}

	@Override
	protected Collection<? extends GrantedAuthority> loadUserAuthorities(DirContextOperations userData, String username,
			String password) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		return authorities;
	}

	private BadCredentialsException badCredentials() {
		return new BadCredentialsException(
				this.messages.getMessage("lewensportalAuthenticationProvider.badCredentials", "Bad credentials"));
	}

	private BadCredentialsException badCredentials(Throwable cause) {
		return (BadCredentialsException) badCredentials().initCause(cause);
	}

}
