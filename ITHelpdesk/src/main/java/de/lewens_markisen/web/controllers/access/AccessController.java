package de.lewens_markisen.web.controllers.access;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.lewens_markisen.domain.Access;
import de.lewens_markisen.services.AccessService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/accesses")
@Controller
public class AccessController {

	private final AccessService accessService;

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

	@RequestMapping(value = "/{id}")
	public ModelAndView showEditAccessForm(@PathVariable(name = "id") Long id) {
		ModelAndView modelAndView = new ModelAndView("access/accessEdit");
		Optional<Access> accessOpt = accessService.findById(id);
		if (accessOpt.isPresent()) {
			modelAndView.addObject("access", accessOpt.get());
		} else {
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String SaveAccess(@ModelAttribute("access") Access access, @RequestParam(value="action", required=true) String action) {
        if (action.equals("save")) {
        	accessService.save(access);
        }
        return "redirect:/";
    }

	@GetMapping("/new")
	public String initCreationForm(Model model) {
		model.addAttribute("access", Access.builder().build());
		return "access/createAccess";
	}

	@PostMapping("/new")
	public String processCreationForm(Access access) {
		//@formatter:off
		Access newAccess = Access.builder()
				.name(access.getName())
				.domain(access.getDomain())
				.user(access.getUser())
				.password(access.getPassword())
				.description(access.getDescription())
				.build();
		//@formatter:on
		Access savedAccess = accessService.save(newAccess);
		return "redirect:/accesses/list";
	}

}
