package de.lewens_markisen.web.controllers;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.lewens_markisen.domain.BaseEntity;
import de.lewens_markisen.person.Person;
import de.lewens_markisen.person.PersonService;
import de.lewens_markisen.person.Persons;
import de.lewens_markisen.services.connection.BCWebService;
import de.lewens_markisen.timeReport.TimeReportService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/persons")
public class PersonController {

	public static Comparator<BaseEntity> COMPARATOR_BY_ID = Comparator.comparing(BaseEntity::getId);
	public static Comparator<Person> COMPARATOR_BY_NAME = Comparator.comparing(Person::getName);

	private final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

	private final PersonService personService;
	private final BCWebService bcWebService;
	private final TimeReportService timeReportService;

	@GetMapping(path = "/list")
	public String list(@RequestParam(defaultValue = "1") int page, Model model) {
		Persons persons = new Persons();
		Page<Person> paginated = findPaginated(page);
		persons.getPersonList().addAll(paginated.toList());
		return addPaginationModel(page, paginated, model);
	}

	private String addPaginationModel(int page, Page<Person> paginated, Model model) {
		List<Person> persons = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("persons", persons);
		return "persons/personsList";
	}

	private Page<Person> findPaginated(int page) {
		int pageSize = 12;
		Sort sort = Sort.by("name").ascending();
		Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
		return personService.findAll(pageable);
	}

	@RequestMapping(value = "/edit/{id}")
	@Transactional
	public ModelAndView showEditPersonForm(@PathVariable(name = "id") Long id) {
		ModelAndView modelAndView = new ModelAndView("persons/personEdit");
		Optional<Person> personOpt = personService.findById(id);
		if (personOpt.isPresent()) {
			modelAndView.addObject("person", personOpt.get());
		} else {
			modelAndView.addObject("message", "Person mit id wurde nicht gefunden!");
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@Transactional
	public String updatePerson(@ModelAttribute("person") Person person,
			@RequestParam(value = "action", required = true) String action) {
		if (action.equals("update")) {
			personService.updatePerson(person);
		}
		return "redirect:/persons";
	}

	@RequestMapping(value = "/delete/{bcCode}", method = RequestMethod.GET)
	public String deletePerson(@PathVariable(name = "id") Long id) {
		Optional<Person> personOpt = personService.findById(id);
		if (personOpt.isPresent()) {
			personService.delete(personOpt.get());
		} else {
		}
		return "redirect:/persons";
	}

	@RequestMapping(value = "/timereport/{bcCode}")
	public ModelAndView showTimeReport(@PathVariable(name = "bcCode") String bcCode) {
		ModelAndView modelAndView = new ModelAndView("persons/timereport");
		System.out.println("showTimeReport! "+bcCode);
		Optional<Person> personOpt = personService.findByBcCode(bcCode);
		if (personOpt.isPresent()) {
			modelAndView.addObject("person", personOpt.get().toString());
			modelAndView.addObject("timeRecords", timeReportService.createReport(personOpt.get().getBcCode()).get());
		} else {
			modelAndView.addObject("message", "Person mit id wurde nicht gefunden!");
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public String loadPerson() {
		bcWebService.loadPersonFromBC();
		return "redirect:/persons";
	}


}
