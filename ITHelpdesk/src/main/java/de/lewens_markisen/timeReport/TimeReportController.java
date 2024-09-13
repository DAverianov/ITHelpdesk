package de.lewens_markisen.timeReport;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import de.lewens_markisen.services.TimeReportService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/timeReport")
@Controller
public class TimeReportController {

	private final TimeReportService timeReportService;

	@GetMapping("/{bcCode}")
	public ModelAndView showEditAccessForm(@PathVariable(name = "bcCode") Integer bcCode) {
		ModelAndView modelAndView = new ModelAndView("timeReport/timeReport");
		Optional<TimeReport> reportOpt = timeReportService.createReport(Integer.toString(bcCode));
		if (reportOpt.isPresent()) {
			modelAndView.addObject("timeReport", reportOpt.get());
		} else {
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

}
