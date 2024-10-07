package de.lewens_markisen.web.controllers;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import de.lewens_markisen.security.perms.PersonTimeReportPermission;
import de.lewens_markisen.security.perms.TimeReportPermission;
import de.lewens_markisen.timeReport.TimeReport;
import de.lewens_markisen.timeReport.TimeReportService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/timeReport")
@Controller
public class TimeReportController {

	private final TimeReportService timeReportService;

	@PersonTimeReportPermission
	@GetMapping("/{bcCode}")
	public ModelAndView showEditAccessForm(@PathVariable(name = "bcCode") Integer bcCode) {
		ModelAndView modelAndView = new ModelAndView("timeReport/timeReport");
		Optional<TimeReport> reportOpt = timeReportService.createReport(Integer.toString(bcCode));
		if (reportOpt.isPresent()) {
			modelAndView.addObject("timeReport", reportOpt.get());
		} else {
			modelAndView.addObject("message", "Datem für user "+getUser()+" wurde nicht bekommen!");
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}
	
	@TimeReportPermission
	@GetMapping("/me")
	public ModelAndView showEditAccessForm() {
		ModelAndView modelAndView = new ModelAndView("timeReport/timeReport");
		Optional<TimeReport> reportOpt = timeReportService.createReportCurrentUser();
		if (reportOpt.isPresent()) {
			modelAndView.addObject("timeReport", reportOpt.get());
		} else {
			modelAndView.addObject("message", "Person für user "+getUser()+" wurde nicht gefunden!");
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

	private String getUser() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

}
