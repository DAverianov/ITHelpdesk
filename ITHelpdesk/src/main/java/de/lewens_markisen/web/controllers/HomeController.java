package de.lewens_markisen.web.controllers;

import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

	@GetMapping(path = { "/", "/home" })
	public String home(Model model) {
		model.addAttribute("message", "Spring Boot IT Helpdesk!");
		return "home";
	}

	@GetMapping("/user")
	public Authentication getLoggedUserDeatil() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// get username
		String username = auth.getName();
		// concat list of authorities to single string seperated by comma
		String authorityString = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		// check if the user have authority -roleA
		String role = "role_A";
		boolean isCurrentUserInRole = auth.getAuthorities().stream().anyMatch(role::equals);
		// return Authentication object
		return auth;
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			request.getSession().invalidate();
		}
		return "redirect:/";
	}
}
