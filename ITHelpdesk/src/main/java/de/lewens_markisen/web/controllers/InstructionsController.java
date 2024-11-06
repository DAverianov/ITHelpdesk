package de.lewens_markisen.web.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.lewens_markisen.domain.local_db.instruction.Instruction;
import de.lewens_markisen.instruction.InstructionList;
import de.lewens_markisen.instruction.InstructionService;
import de.lewens_markisen.security.perms.InstructionCreatePermission;
import de.lewens_markisen.security.perms.InstructionReadPermission;
import de.lewens_markisen.web.controllers.playlocad.InstructionLinePayload;
import de.lewens_markisen.web.controllers.playlocad.InstructionPayload;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/instructions")
@Controller
public class InstructionsController {
	
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

	@InstructionCreatePermission
	@GetMapping("/create")
	public String getNewInstructionPage(Model model) {
		//@formatter:off
		model.addAttribute("payload", InstructionPayload.builder().build());
		//@formatter:on
		return "instructions/new_instruction";
	}

	//@formatter:off
	@InstructionCreatePermission
	@PostMapping("/create")
	public String createInstruction(
			@Valid InstructionPayload payload, 
			@Valid List<InstructionLinePayload> payload_lines, 
			BindingResult bindingResult, 
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("payload", payload);
			model.addAttribute("errors",
					bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).toList());
			return "instructions/new_instruction";
		} else {
			Instruction instr = Instruction.builder()
					.name(payload.getName())
					.description(payload.getDescription())
					.build();
			if (payload_lines!=null) {
				for (InstructionLinePayload l: payload_lines) {
					instr.addLine(l);
				}
			}
			this.instructionService.save(instr);
			return "redirect:/instructions/list";
		}
	}
	//@formatter:on

}
