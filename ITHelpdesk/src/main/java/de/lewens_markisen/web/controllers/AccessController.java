package de.lewens_markisen.web.controllers;

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

import de.lewens_markisen.access.Access;
import de.lewens_markisen.access.AccessService;
import de.lewens_markisen.access.Accesses;
import de.lewens_markisen.security.perms.AccessCreatePermission;
import de.lewens_markisen.security.perms.AccessReadPermission;
import de.lewens_markisen.security.perms.AccessUpdatePermission;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/accesses")
@Controller
public class AccessController {

	private final AccessService accessService;

	@AccessReadPermission
	@GetMapping(path = "/list")
	public String list(@RequestParam(defaultValue = "1") int page, Model model) {
		Accesses accesses = new Accesses();
		Page<Access> paginated = findPaginated(page);
		accesses.getAccessList().addAll(paginated.toList());
		return addPaginationModel(page, paginated, model);
	}

	private String addPaginationModel(int page, Page<Access> paginated, Model model) {
		List<Access> accesses = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("accesses", accesses);
		return "access/accessList";
	}

	private Page<Access> findPaginated(int page) {
		int pageSize = 12;
		Sort sort = Sort.by("name").ascending();
		Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
		return accessService.findAll(pageable);
	}

	@AccessReadPermission
	@RequestMapping(value = "/{id}")
	public ModelAndView showEditAccessForm(@PathVariable(name = "id") Long id) {
		ModelAndView modelAndView = new ModelAndView("access/accessEdit");
		Optional<Access> accessOpt = accessService.findById(id);
		if (accessOpt.isPresent()) {
			modelAndView.addObject("access", accessOpt.get());
		} else {
			modelAndView.addObject("message", "Access mit id wurde nicht gefunden!");
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

	@AccessCreatePermission
	@GetMapping("/new")
	public String initCreationForm(Model model) {
		model.addAttribute("access", Access.builder().build());
		return "access/createAccess";
	}

	@AccessCreatePermission
	@PostMapping("/new")
	public String processCreationForm(Access access) {
		//@formatter:off
		Access newAccess = Access.builder()
				.name(access.getName())
				.url(access.getUrl())
				.domain(access.getDomain())
				.user(access.getUser())
				.password(access.getPassword())
				.description(access.getDescription())
				.build();
		//@formatter:on
		Access savedAccess = accessService.save(newAccess);
		return "redirect:/accesses/list";
	}

	@AccessUpdatePermission
	@PostMapping(value = "/update")
    public String update(@ModelAttribute("access") Access access, @RequestParam(value="action", required=true) String action) {
		System.out.println("ich bin bei Update");
     	if (action.equals("update")) {
        	accessService.update(access);
        }
        return "redirect:/accesses/list";
    }

}
