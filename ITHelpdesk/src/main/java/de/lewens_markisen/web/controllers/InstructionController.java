package de.lewens_markisen.web.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.lewens_markisen.domain.local_db.instruction.Instruction;
import de.lewens_markisen.domain.local_db.instruction.InstructionLine;
import de.lewens_markisen.instruction.InstructionService;
import de.lewens_markisen.security.perms.InstructionCreatePermission;
import de.lewens_markisen.security.perms.InstructionUpdatePermission;
import de.lewens_markisen.web.controllers.playlocad.InstructionLinePayload;
import de.lewens_markisen.web.controllers.playlocad.InstructionPayload;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/instruction/{instructionId:\\d+}")
public class InstructionController {
	private final InstructionService instructionService;

	@ModelAttribute("instruction")
	public Instruction instruction(@PathVariable("instructionId") int instructionId) {
		return this.instructionService.findById((long) instructionId)
				.orElseThrow(() -> new NoSuchElementException("instruction.not_found"));
	}

	//@formatter:off
	@InstructionCreatePermission
	@PostMapping("newline")
	public String newLine(
			@Valid @RequestBody InstructionPayload payload, 
			Model model) {
		log.debug("__/newline");
		model.addAttribute("payload", payload);
		return "instructions/new_instruction";
	}
	//@formatter:on

	@InstructionUpdatePermission
	@GetMapping(value = "edit")
	@Transactional
	public ModelAndView showEditInstructionForm(@PathVariable(name = "instructionId") int id) {
		ModelAndView modelAndView = new ModelAndView("instructions/instructionEdit");
		Optional<Instruction> instrOpt = instructionService.findById((long) id);
		if (instrOpt.isPresent()) {
			modelAndView.addObject("instruction", instrOpt.get());
			modelAndView.addObject("payload", createInstructionPayload(instrOpt.get()));
		} else {
			modelAndView.addObject("message", "Instruction mit id wurde nicht gefunden!");
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

	@InstructionUpdatePermission
	@PostMapping(value = "edit")
	@Transactional
	public String updateInstruction(@ModelAttribute(name = "instruction", binding = false) Instruction instruction,
			InstructionPayload payload, 
			BindingResult result,
			@RequestParam(value = "action", required = true) String action) {
		if (action.equals("update")) {
			System.out.println("..."+payload);
			Instruction instr = updateFields(instruction, payload);
			instructionService.save(instr);
		}
		return "redirect:/instructions/list";
	}

	private Instruction updateFields(Instruction instr, @Valid InstructionPayload payload) {
		instr.setName(payload.getName());
		instr.setDescription(payload.getDescription());

		List<InstructionLine> lines = new ArrayList<InstructionLine>();
		payload.getLines().stream().filter(l -> !l.getDescription().isBlank())
				.forEach(l -> lines.add(new InstructionLine(instr, l.getStringNummer(), l.getDescription())));
		instr.setInstructionLines(lines);
		return instr;
	}

	private InstructionPayload createInstructionPayload(Instruction instruction) {
		//@formatter:off
		InstructionPayload payload = InstructionPayload.builder()
				.id(instruction.getId())
				.name(instruction.getName())
				.description(instruction.getDescription())
				.build();
		payload.setLines(createInstructionLinesPayload(instruction));
		return payload;
		//@formatter:on
	}

	private List<InstructionLinePayload> createInstructionLinesPayload(Instruction instruction) {
		List<InstructionLinePayload> lines = new ArrayList<InstructionLinePayload>();
		if (instruction != null) {
			instruction.getInstructionLines().stream().forEach(l -> lines.add(new InstructionLinePayload(l)));
		}
		int lineCount = lines.size();
		while (lineCount <= 4) {
			lineCount++;
			lines.add(InstructionLinePayload.builder().id(1000l).stringNummer(lineCount).build());
		}
		lines.sort(Comparator.comparingInt(InstructionLinePayload::getStringNummer));
		return lines;
	}

}
