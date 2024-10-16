package de.lewens_markisen.web.controllers;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.lewens_markisen.domain.localDb.BaseEntity;
import de.lewens_markisen.domain.localDb.security.UserSpring;
import de.lewens_markisen.domain.localDb.security.UserSpringList;
import de.lewens_markisen.security.UserSpringService;
import de.lewens_markisen.security.perms.UserDeletePermission;
import de.lewens_markisen.security.perms.UserReadPermission;
import de.lewens_markisen.security.perms.UserUpdatePermission;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
public class UserSpringController {

	public static Comparator<BaseEntity> COMPARATOR_BY_ID = Comparator.comparing(BaseEntity::getId);
	public static Comparator<UserSpring> COMPARATOR_BY_NAME = Comparator.comparing(UserSpring::getUsername);

	private final UserSpringService userSpringService;

	@UserReadPermission
	@GetMapping(path = "/list")
	public String list(@RequestParam(defaultValue = "1") int page, Model model) {
		UserSpringList users = new UserSpringList();
		Page<UserSpring> paginated = findPaginated(page);
		users.getUserSpringList().addAll(paginated.toList());
		return addPaginationModel(page, paginated, model);
	}

	private String addPaginationModel(int page, Page<UserSpring> paginated, Model model) {
		List<UserSpring> users = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("users", users);
		return "users/usersList";
	}

	private Page<UserSpring> findPaginated(int page) {
		int pageSize = 50;
		Sort sort = Sort.by("username").ascending();
		Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
		return userSpringService.findAll(pageable);
	}

	@UserUpdatePermission
	@GetMapping(value = "/edit/{id}")
	@Transactional
	public ModelAndView showEditUserForm(@PathVariable(name = "id") Integer id) {
		ModelAndView modelAndView = new ModelAndView("users/userEdit");
		Optional<UserSpring> userOpt = userSpringService.findById(id);
		if (userOpt.isPresent()) {
			modelAndView.addObject("user", userOpt.get());
		} else {
			modelAndView.addObject("message", "User mit id wurde nicht gefunden!");
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

	@UserUpdatePermission
	@PostMapping(value = "/update")
	@Transactional
	public String updateUser(@ModelAttribute("user") UserSpring user,
			@RequestParam(value = "action", required = true) String action) {
		if (action.equals("update")) {
			try {
				userSpringService.saveUser(user);
			}
			catch (Exception e) {
				log.debug("could not save User: " + user.getUsername()+". Not unique!");
			}
		}
		return "redirect:/users/list";
	}

	@UserDeletePermission
	@GetMapping(value = "/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id) {
		Optional<UserSpring> userSpringOpt = userSpringService.findById(id);
		if (userSpringOpt.isPresent()) {
			userSpringService.delete(userSpringOpt.get());
		} else {
		}
		return "redirect:/users/list";
	}

	@UserUpdatePermission
	@GetMapping(path = "/rewriteNames")
	public String rewriteUsernames() {
		userSpringService.rewriteUsernames();
		return "redirect:/users/list";
	}

}
