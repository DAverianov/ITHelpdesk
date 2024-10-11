package de.lewens_markisen.web.controllers;

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
import de.lewens_markisen.domain.localDb.Log;
import de.lewens_markisen.log.LogList;
import de.lewens_markisen.log.LogService;
import de.lewens_markisen.security.perms.LogReadPermission;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/logs")
@Controller
public class LogController {

	private final LogService logService;

	@LogReadPermission
	@GetMapping(path = "/list")
	public String list(@RequestParam(defaultValue = "1") int page, Model model) {
		LogList logs = new LogList();
		Page<Log> paginated = findPaginated(page);
		logs.getUserSpringList().addAll(paginated.toList());
		return addPaginationModel(page, paginated, model);
	}

	private String addPaginationModel(int page, Page<Log> paginated, Model model) {
		List<Log> logs = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("logs", logs);
		return "logs/logsList";
	}

	private Page<Log> findPaginated(int page) {
		int pageSize = 50;
		Sort sort = Sort.by("lastModifiedDate").descending();
		Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
		return logService.findAll(pageable);
	}

}
