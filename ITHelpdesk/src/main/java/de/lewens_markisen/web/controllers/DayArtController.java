package de.lewens_markisen.web.controllers;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.lewens_markisen.domain.local_db.security.UserSpring;
import de.lewens_markisen.domain.local_db.time_register_event.DayArt;
import de.lewens_markisen.security.perms.PersonReadPermission;
import de.lewens_markisen.security.perms.UserUpdatePermission;
import de.lewens_markisen.timeRegisterEvent.DayArtService;
import de.lewens_markisen.web.controllers.playlocad.UserRolesChecked;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/day_arts")
@Controller
public class DayArtController {

	private final DayArtService dayArtService;

	@PersonReadPermission
	@GetMapping(path = "/list")
	public String list(Model model) {
        model.addAttribute("days", dayArtService.findAll());
        return "day_arts/dayArtList";
	}

	@PersonReadPermission
	@GetMapping(value = "/edit/{id}")
	@Transactional
	public ModelAndView showEditUserForm(@PathVariable(name = "id") Long id) {
		ModelAndView modelAndView = new ModelAndView("day_arts/dayArtEdit");
		Optional<DayArt> dayArtOpt = dayArtService.findById(id);
		if (dayArtOpt.isPresent()) {
			modelAndView.addObject("dayArt", dayArtOpt.get());
		} else {
			modelAndView.addObject("message", "Art of day whith id not found!");
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

	@PersonReadPermission
	@PostMapping(value = "/update")
	@Transactional
	public String updateUser(@ModelAttribute("dayArt") DayArt dayArt,
			@RequestParam(value = "action", required = true) String action) {
		if (action.equals("update")) {
			try {
				Optional<DayArt> dayArtFetchedOpt = dayArtService.findById(dayArt.getId());
				if(dayArtFetchedOpt.isEmpty()) {
					dayArtService.save(dayArt);
				}
				else if (dayArtFetchedOpt.get().getId().equals(dayArt.getId())) {
					DayArt dayToSave = dayArtFetchedOpt.get();
					dayToSave.setName(dayArt.getName());
					dayToSave.setMinuten(dayArt.getMinuten());
					dayArtService.save(dayToSave);
				}
				else {
					log.debug("could not save Art of days: " + dayArt.getName()+". Not unique!");
				}
			}
			catch (Exception e) {
				log.debug("could not save Art of days: " + dayArt.getName()+". Not unique!"+e);
			}
		}
		return "redirect:/day_arts/list";
	}

}
