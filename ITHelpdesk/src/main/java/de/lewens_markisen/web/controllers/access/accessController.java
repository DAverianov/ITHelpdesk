package de.lewens_markisen.web.controllers.access;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.lewens_markisen.domain.Access;
import de.lewens_markisen.repositories.AccessRepository;
import de.lewens_markisen.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/accesses")
@Controller
public class accessController {

	private final AccessRepository accessRepository;

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
		return accessRepository.findAll(pageable);
	}

}


