package de.lewens_markisen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import de.lewens_markisen.services.TimeRegisterEventService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/zeiterfassungsystem")
@Controller 
public class TimeRegisterEventController {

    private final TimeRegisterEventService timeRegisterEventService;

    public TimeRegisterEventController(TimeRegisterEventService timeRegisterEventService) {
        this.timeRegisterEventService = timeRegisterEventService;
    }

    @GetMapping("/{personId}")
    public String getWorkTime(Model model, @PathVariable Long personId) {
    	if (personId==null) {
    		return "error";
    	}
        model.addAttribute("workTime", timeRegisterEventService.findAll(personId));
        return "workTime";
    }
}
