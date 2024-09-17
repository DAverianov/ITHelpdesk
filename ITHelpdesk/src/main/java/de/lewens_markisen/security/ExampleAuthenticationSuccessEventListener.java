package de.lewens_markisen.security;

import java.util.Optional;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.ldap.userdetails.InetOrgPerson;

import de.lewens_markisen.domain.security.User;
import de.lewens_markisen.services.security.UserService;

public class ExampleAuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

	  private final UserService userService;

	  public ExampleAuthenticationSuccessEventListener(UserService userService) {
	    this.userService = userService;
	  }

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		InetOrgPerson principal = (InetOrgPerson) event.getAuthentication().getPrincipal();
		Optional<User> userOpt = userService.getUserByName(principal.getUsername());
		if (userOpt.isEmpty()) {
			User user = userService.createUser(principal.getUsername(), principal.getPassword());
		} 
	}

}
