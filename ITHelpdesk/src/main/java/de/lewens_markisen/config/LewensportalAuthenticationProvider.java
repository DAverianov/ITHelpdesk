package de.lewens_markisen.config;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import de.lewens_markisen.domain.local_db.security.UserSpring;
import de.lewens_markisen.security.LssUser;
import de.lewens_markisen.security.LssUserService;
import de.lewens_markisen.security.UserSpringService;
import de.lewens_markisen.utils.StringUtilsLss;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Getter
@Component
public class LewensportalAuthenticationProvider implements AuthenticationProvider {

	protected final Log logger = LogFactory.getLog(getClass());

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	private final UserSpringService userService;
	private final LssUserService lssUserService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final String username = authentication.getName();
		final String password = authentication.getCredentials().toString();
		try {
			return authenticateAgainstThirdPartyAndGetAuthentication(username, password);
		} catch (UsernameNotFoundException ex) {
			throw badCredentials(ex);
		}
	}

	private UsernamePasswordAuthenticationToken authenticateAgainstThirdPartyAndGetAuthentication(String username,
			String password) throws UsernameNotFoundException {

		Optional<List<LssUser>> lssUsersOpt;
		
		UsernameNotFoundException userNameNotFoundException = new UsernameNotFoundException(
				"User " + username + " not found in lewensportal DB!");
		try {
			lssUsersOpt = lssUserService.findUserByName(username);
		} catch (Exception e) {
			throw badCredentials(userNameNotFoundException);
		}
		if (lssUsersOpt.isEmpty()) {
			throw badCredentials(userNameNotFoundException);
		}
		
		userNameNotFoundException = new UsernameNotFoundException(
				"User " + username + " not correct password!");
		try {
			for (LssUser lssUser : lssUsersOpt.get()) {
				if (lssUserService.getLssHashPassword(password, lssUser.getSalt()).equals(lssUser.getPassword())) {
					//@formatter:off
					UserSpring principal = UserSpring.builder()
							.username(username)
							.password(password).build();
					//@formatter:on
					return authoriseInLocalDB(new UsernamePasswordAuthenticationToken(principal, password));
				}
			}
		} catch (NoSuchAlgorithmException e) {
			userNameNotFoundException = new UsernameNotFoundException("NoSuchAlgorithmException", e);
			throw badCredentials(userNameNotFoundException);
		}
		throw badCredentials(userNameNotFoundException);
	}

	@Transactional
	public UsernamePasswordAuthenticationToken authoriseInLocalDB(UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {

		String userName = StringUtilsLss.convertNameToLowCase(authentication.getName());
		Optional<UserSpring> userOpt = userService.getUserByName(userName);
		UserSpring user;
		if (userOpt.isPresent()) {
			user = userOpt.get();
		} else {
			user = userService.createUser(userName, "");
		}
		user = userService.fillAttributsFromLss(user);
		return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	private BadCredentialsException badCredentials() {
		return new BadCredentialsException(
				this.messages.getMessage("lewensportalAuthenticationProvider.badCredentials", "Bad credentials"));
	}

	private BadCredentialsException badCredentials(Throwable cause) {
		log.debug(cause.getMessage());
		return (BadCredentialsException) badCredentials().initCause(cause);
	}

}
