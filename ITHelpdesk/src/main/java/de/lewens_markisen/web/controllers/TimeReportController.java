package de.lewens_markisen.web.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.person.PersonService;
import de.lewens_markisen.security.UserSpringService;
import de.lewens_markisen.security.perms.PersonTimeReportPermission;
import de.lewens_markisen.security.perms.TimeReportPermission;
import de.lewens_markisen.timeReport.PeriodReport;
import de.lewens_markisen.timeReport.TimeReport;
import de.lewens_markisen.timeReport.TimeReportService;
import de.lewens_markisen.utils.DateUtils;
import de.lewens_markisen.utils.FileOperations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/timeReport")
@Controller
public class TimeReportController {

	private final TimeReportService timeReportService;
	private final UserSpringService userSpringService;
	private final PersonService personService;

	@PersonTimeReportPermission
	@GetMapping("/{bcCode}")
	public ModelAndView timeReportForPerson(
			@PathVariable(name = "bcCode") Integer bcCode) {
		ModelAndView modelAndView = new ModelAndView(
				"timeReport/timeReportPeriod");
		Optional<TimeReport> reportOpt = timeReportService
				.createReport(Integer.toString(bcCode));
		if (reportOpt.isPresent()) {
			modelAndView.addObject("timeReport", reportOpt.get());
		} else {
			modelAndView.addObject("message",
					"Daten für user "
							+ userSpringService.getAuthenticationName()
							+ " wurde nicht bekommen!");
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

	@TimeReportPermission
	@PostMapping(value = "/pdf")
	public ResponseEntity<byte[]> timeReportPDF(@RequestParam String bcCode) {

		FileOperations fileOp = new FileOperations();
		File file = fileOp.getFileFromResources("timeReport.pdf");
		log.debug(".. send file " + file + " for Person " + bcCode);
		try {
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\"" + file.getName() + "\"")
					.body(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("..error by read file " + file);
		}
		return ResponseEntity.notFound().build();
	}

	@TimeReportPermission
	@PostMapping(value = "/terminal")
	public String timeReportTerminal(@RequestParam String bcCode,
			@RequestParam String month) {
		Optional<Person> personOpt = personService.findByBcCode(bcCode);
		if (personOpt.isEmpty()) {
			log.debug("BC Code " + bcCode + " was not found!");
			return "Persone nicht gefunden!";
		}
		//@formatter:off
		LocalDate startMonat = DateUtils
				.startMonat(DateUtils.readDateFromString(month, "yyyyMMdd"));
		PeriodReport period = PeriodReport.builder().start(startMonat)
				.end(DateUtils.endMonat(startMonat))
				.build();
		//@formatter:on
		
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setCacheable(false);
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setForceTemplateMode(true);
        templateEngine.setTemplateResolver(templateResolver);

        Context ctx = new Context();

        Optional<TimeReport> timeReportOpt = timeReportService.createReport(personOpt.get(), period);
        if (timeReportOpt.isPresent()) {
 	        ctx.setVariable("timeReport", timeReportOpt.get());
	        String result = templateEngine.process("timeReport/timeReportPeriod.html", ctx);
	        System.out.println("result:" + result);	
	        return result;
        }
        return "";
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
		PeriodReport period = PeriodReport.builder().start(periodStart)
				.end(periodEnd).build();
		if (action.equals("update")) {
		} else if (action.equals("period_minus")) {
			period = PeriodReport
					.periodReportMonth(period.getStart().minusDays(1));
		} else if (action.equals("period_plus")) {
			period = PeriodReport
					.periodReportMonth(period.getEnd().plusDays(1));
		} else {
			ModelAndView modelAndView = new ModelAndView(
					"timeReport/timeReport");
			modelAndView.addObject("message", "Action wasnt found!");
			modelAndView.setViewName("error");
			return modelAndView;
		}
		return outTimeReport(timeReportService.createReport(person, period));
	}

	private ModelAndView outTimeReport(Optional<TimeReport> reportOpt) {
		ModelAndView modelAndView = new ModelAndView(
				"timeReport/timeReportPeriod");
		if (reportOpt.isPresent()) {
			modelAndView.addObject("timeReport", reportOpt.get());
		} else {
			modelAndView.addObject("message",
					"User " + userSpringService.getAuthenticationName()
							+ ": There is no BC Code for the user!");
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

}
