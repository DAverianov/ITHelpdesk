package de.lewens_markisen.web.controllers;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.lewens_markisen.domain.localDb.Person;
import de.lewens_markisen.security.UserSpringService;
import de.lewens_markisen.security.perms.PersonTimeReportPermission;
import de.lewens_markisen.security.perms.TimeReportPermission;
import de.lewens_markisen.timeReport.PeriodReport;
import de.lewens_markisen.timeReport.TimeReport;
import de.lewens_markisen.timeReport.TimeReportService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/timeReport")
@Controller
public class TimeReportController {

	private final TimeReportService timeReportService;
	private final UserSpringService userSpringService;

	@PersonTimeReportPermission
	@GetMapping("/{bcCode}")
	public ModelAndView timeReportForPerson(@PathVariable(name = "bcCode") Integer bcCode) {
		ModelAndView modelAndView = new ModelAndView("timeReport/timeReportPeriod");
		Optional<TimeReport> reportOpt = timeReportService.createReport(Integer.toString(bcCode));
		if (reportOpt.isPresent()) {
			modelAndView.addObject("timeReport", reportOpt.get());
		} else {
			modelAndView.addObject("message", "Daten für user "+userSpringService.getAuthenticationName()+" wurde nicht bekommen!");
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}
	
	@TimeReportPermission
	@GetMapping("/me")
	public ModelAndView timeReportForMe() {
		return outTimeReport(timeReportService.createReportCurrentUser());
	}

	@TimeReportPermission
    @PostMapping("/me")
	public ModelAndView timeReportForMe(
			@ModelAttribute(name = "person") Person person,
			@ModelAttribute(name = "period.start") LocalDate periodStart,
			@ModelAttribute(name = "period.end") LocalDate periodEnd,
			@RequestParam(value = "action", required = true) String action) {
		PeriodReport period = PeriodReport.builder().start(periodStart).end(periodEnd).build();
		if (action.equals("update")) {
		}
		else if (action.equals("period_minus")) {
			period = PeriodReport.periodReportMonth(period.getStart().minusDays(1));
		}
		else if (action.equals("period_plus")) {
			period = PeriodReport.periodReportMonth(period.getEnd().plusDays(1));
		}
		else {
			ModelAndView modelAndView = new ModelAndView("timeReport/timeReport");
			modelAndView.addObject("message", "Action wasnt found!");
			modelAndView.setViewName("error");
			return modelAndView;
		}
		return outTimeReport(timeReportService.createReport(person, period));
    }

	private ModelAndView outTimeReport(Optional<TimeReport> reportOpt) {
		ModelAndView modelAndView = new ModelAndView("timeReport/timeReportPeriod");
		if (reportOpt.isPresent()) {
			modelAndView.addObject("timeReport", reportOpt.get());
		} else {
			modelAndView.addObject("message", "User "+userSpringService.getAuthenticationName()+": There is no BC Code for the user!");
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

}
