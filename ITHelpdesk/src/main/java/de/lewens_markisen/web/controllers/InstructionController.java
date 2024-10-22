package de.lewens_markisen.web.controllers;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.lewens_markisen.domain.local_db.instruction.Instruction;
import de.lewens_markisen.instruction.InstructionList;
import de.lewens_markisen.instruction.InstructionService;
import de.lewens_markisen.security.perms.InstructionCreatePermission;
import de.lewens_markisen.security.perms.InstructionReadPermission;
import de.lewens_markisen.security.perms.UserUpdatePermission;
import de.lewens_markisen.web.controllers.playlocad.InstructionLinePayload;
import de.lewens_markisen.web.controllers.playlocad.NewInstructionPayload;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/instructions")
public class InstructionController {
	private final InstructionService instructionService;
//    private final InstructionRestClient instructionRestClient;

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
		model.addAttribute("payload", NewInstructionPayload.builder().build());
		model.addAttribute("payload_lines", Set.of(InstructionLinePayload.builder().id(1).stringNummer(1).build()));
		//@formatter:on
		return "instructions/new_instruction";
	}

	//@formatter:off
	@InstructionCreatePermission
	@PostMapping("/create")
	public String createInstruction(
			@Valid NewInstructionPayload payload, 
			@Valid Set<InstructionLinePayload> payload_lines, 
			BindingResult bindingResult, 
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("payload", payload);
			model.addAttribute("payload_lines", payload_lines);
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
	//@formatter:off
	@InstructionCreatePermission
	@PostMapping("/newline")
	public String newLine(
			@Valid NewInstructionPayload payload, 
			@Valid Set<InstructionLinePayload> payload_lines, 
			BindingResult bindingResult, 
			Model model) {

		model.addAttribute("payload", payload);
			model.addAttribute("payload_lines", payload_lines.add(InstructionLinePayload.builder().id(1).stringNummer(1).build()));
			return "instructions/new_instruction";
	}
	//@formatter:on

	@UserUpdatePermission
	@GetMapping(value = "/edit/{id}")
	@Transactional
	public ModelAndView showEditInstructionForm(@PathVariable(name = "id") Long id) {
		ModelAndView modelAndView = new ModelAndView("instructions/instructionEdit");
		Optional<Instruction> instrOpt = instructionService.findById(id);
		if (instrOpt.isPresent()) {
			modelAndView.addObject("instruction", instrOpt.get());
		} else {
			modelAndView.addObject("message", "Instruction mit id wurde nicht gefunden!");
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

	@UserUpdatePermission
	@PostMapping(value = "/update")
	@Transactional
	public String updateInstruction(@ModelAttribute("instruction") Instruction instruction,
			@RequestParam(value = "action", required = true) String action) {
		if (action.equals("update")) {
			try {
				Optional<Instruction> instructionFetchedOpt = instructionService.findById(instruction.getId());
				if(instructionFetchedOpt.isEmpty()) {
					log.debug("could not save Instruction: " + instruction.getName()+". Not found!");
				}
				else if (instructionFetchedOpt.get().getId().equals(instruction.getId())) {
					instructionService.save(instruction);
				}
			}
			catch (Exception e) {
				log.debug("could not save Instruction: " + instruction.getName()+". Not unique!");
			}
		}
		return "redirect:/instructions/list";
	}

}
