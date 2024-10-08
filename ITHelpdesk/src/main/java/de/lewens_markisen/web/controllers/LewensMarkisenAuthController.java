package de.lewens_markisen.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.lewens_markisen.security.perms.AccessCreatePermission;
import de.lewens_markisen.web.controllers.playlocad.AuthPayload;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/lewensmarkisen")
@Controller
public class LewensMarkisenAuthController {

	@AccessCreatePermission
	@GetMapping("/auth")
	public String initCreationForm() {
		return "lewensmarkisen/auth";
	}

	@PostMapping("/auth")
	public String createProduct(AuthPayload payload, Model model, HttpServletResponse response) {

		String res = " user: "+payload.userName()+". Pass: "+payload.password();
		System.out.println(res);
        model.addAttribute("payload", payload);
        model.addAttribute("errors", res);
		return "lewensmarkisen/auth";
	}

}
