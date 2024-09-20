package de.lewens_markisen.config;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;

import jakarta.servlet.http.HttpSession;

public class LoginListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

	@Autowired(required = false)
	HttpSession httpSession;

//	@Autowired
//	Environment env;

	@Override
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent appEvent) {
		System.out.println("..login");
//		if (appEvent != null) {
//			LdapUserDetailsImpl ldapUserDetailsImpl = (LdapUserDetailsImpl) appEvent.getAuthentication().getPrincipal();
//			try {
//				if (ldapUserDetailsImpl != null) {
//
//					logger.info("Session Created for " + ldapUserDetailsImpl.getUsername());
//
//					if (httpSession.getAttribute("adminUser") == null) {
//						// check user is admin and set into session
//						if (isAdminUser(ldapUserDetailsImpl.getUsername())) {
//							httpSession.setAttribute("adminUser", "ADMIN_USER");
//							Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//							List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(auth.getAuthorities());
//							// Add the ROLE_ADMIN into Authorities
//							authorities.add(new SimpleGrantedAuthority(SecurityConfig.ADMIN));
//							// Create a new Authentication based on current principal and authorities and
//							// set into Security Context
//							Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(),
//									auth.getCredentials(), authorities);
//							SecurityContextHolder.getContext().setAuthentication(newAuth);
//						}
//					}
//				}
//			} catch (Exception e) {
//				logger.error("Exception occurred : " + e.getMessage());
//			}
//		}
	}
}
