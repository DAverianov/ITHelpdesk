package de.lewens_markisen.web.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.lewens_markisen.security.LssUser;
import de.lewens_markisen.security.LssUserService;
import de.lewens_markisen.security.perms.AccessCreatePermission;
import de.lewens_markisen.web.controllers.playlocad.AuthPayload;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/lewensportal")
@Controller
public class LewensportalAuthController {
	
	private final LssUserService lssUserService;
	
	@AccessCreatePermission
	@GetMapping("/auth")
	public String initCreationForm() {
		return "lewensportal/auth";
	}

	@PostMapping("/auth")
	public String createProduct(AuthPayload payload, Model model, HttpServletResponse response) {

		List<String> res = new ArrayList<String>();
		res.add(" user: "+payload.userName()+". Pass: "+payload.password());
		
		Optional<LssUser> lssUser = lssUserService.findUserByName(payload.userName());
		if (lssUser.isPresent()) {
			res.add(""+lssUser);
		}
		else {
			res.add("    not found in lewensportal!");
		}
		
        model.addAttribute("payload", payload);
        model.addAttribute("errors", res);
		return "lewensportal/auth";
	}

}
