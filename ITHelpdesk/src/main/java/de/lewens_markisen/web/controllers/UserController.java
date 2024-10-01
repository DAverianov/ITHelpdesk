package de.lewens_markisen.web.controllers;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.lewens_markisen.domain.BaseEntity;
import de.lewens_markisen.domain.security.UserSpring;
import de.lewens_markisen.domain.security.UserSpringList;
import de.lewens_markisen.services.security.UserSpringService;
import de.lewens_markisen.services.security.UserSpringServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
public class UserController {

	public static Comparator<BaseEntity> COMPARATOR_BY_ID = Comparator.comparing(BaseEntity::getId);
	public static Comparator<UserSpring> COMPARATOR_BY_NAME = Comparator.comparing(UserSpring::getUsername);

	private final UserSpringService userSpringService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
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
		int pageSize = 12;
		Sort sort = Sort.by("username").ascending();
		Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
		return userSpringService.findAll(pageable);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/edit/{id}")
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

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@Transactional
	public String updateUser(@ModelAttribute("user") UserSpring user,
			@RequestParam(value = "action", required = true) String action) {
		if (action.equals("update")) {
			userSpringService.updateUser(user);
		}
		return "redirect:/users/list";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deleteUser(@PathVariable(name = "id") Integer id) {
		Optional<UserSpring> userSpringOpt = userSpringService.findById(id);
		if (userSpringOpt.isPresent()) {
			userSpringService.delete(userSpringOpt.get());
		} else {
		}
		return "redirect:/users/list";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(path = "/rewriteNames")
	public String rewriteUsernames() {
		userSpringService.rewriteUsernames();
		return "users/list";
	}

}
