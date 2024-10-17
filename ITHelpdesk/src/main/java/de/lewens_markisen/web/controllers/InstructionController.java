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

import de.lewens_markisen.domain.localDb.instruction.Instruction;
import de.lewens_markisen.instruction.InstructionList;
import de.lewens_markisen.instruction.InstructionService;
import de.lewens_markisen.security.perms.InstructionReadPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/intsructions")
public class InstructionController {
	private final InstructionService instructionService;

	@InstructionReadPermission
	@GetMapping(path = "/list")
	public String list(@RequestParam(defaultValue = "1") int page, Model model) {
		InstructionList instructions = new InstructionList();
		Page<Instruction> paginated = findPaginated(page);
		instructions.getInstructionList().addAll(paginated.toList());
		return addPaginationModel(page, paginated, model);
	}

	private String addPaginationModel(int page, Page<Instruction> paginated, Model model) {
		List<Instruction> instr = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("instructions", instr);
		return "instructions/instructionsList";
	}

	private Page<Instruction> findPaginated(int page) {
		int pageSize = 50;
		Sort sort = Sort.by("name").ascending();
		Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
		return instructionService.findAll(pageable);
	}

}
