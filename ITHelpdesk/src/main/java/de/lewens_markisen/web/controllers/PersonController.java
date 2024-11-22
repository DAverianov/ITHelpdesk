package de.lewens_markisen.web.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.lewens_markisen.domain.local_db.BaseEntity;
import de.lewens_markisen.domain.local_db.person.DefferedEvent;
import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.person.PersonDefferedEvent;
import de.lewens_markisen.domain.local_db.person.PersonPresence;
import de.lewens_markisen.domain.local_db.security.UserSpring;
import de.lewens_markisen.person.PersonDefferedEventService;
import de.lewens_markisen.person.PersonPresenceService;
import de.lewens_markisen.person.PersonService;
import de.lewens_markisen.security.UserSpringService;
import de.lewens_markisen.security.perms.PersonDeletePermission;
import de.lewens_markisen.security.perms.PersonLoadPermission;
import de.lewens_markisen.security.perms.PersonReadPermission;
import de.lewens_markisen.security.perms.PersonUpdatePermission;
import de.lewens_markisen.services.connection.BCWebServiceLoadZeitnachweis;
import de.lewens_markisen.services.connection.BCWebServiceLoadPerson;
import de.lewens_markisen.web.controllers.playlocad.PersonWithGlock;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/persons")
public class PersonController {

	public static Comparator<BaseEntity> COMPARATOR_BY_ID = Comparator.comparing(BaseEntity::getId);
	public static Comparator<Person> COMPARATOR_BY_NAME = Comparator.comparing(Person::getName);

	private final PersonService personService;
	private final PersonPresenceService personPresenceService;
	private final PersonDefferedEventService personDefferedEventService;
	private final UserSpringService userService;
	private final BCWebServiceLoadZeitnachweis bcWebServiceLoadZeitnachweis;
	private final BCWebServiceLoadPerson bcWebServicePersonLoad;

	@PersonReadPermission
	@GetMapping(path = "/list")
	public String list(@RequestParam(defaultValue = "1") int page, Model model) {
		Page<Person> paginated = findPaginated(page, "");
		return addPaginationModel(page, paginated, model, "");
	}

	@PersonReadPermission
	@PostMapping(path = "/list")
	public String listPost(@ModelAttribute(name = "findField") String findField,
			@RequestParam(defaultValue = "1") int page, 
			@RequestParam(value = "action", required = true) String action,
			Model model) {
		if (action.equals("clearFilter")) {
			findField = "";
		}
		Page<Person> paginated = findPaginated(page, findField);
		return addPaginationModel(page, paginated, model, findField);
	}

	private String addPaginationModel(int page, Page<Person> paginated, Model model, String findField) {
		List<Person> persons = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("persons", addPresence(convertToPersonWithGlock(persons)));
		model.addAttribute("findField", findField);
		model.addAttribute("userHasEmail", userService.userHasEmail());
		return "persons/personsList";
	}

	private Object addPresence(List<PersonWithGlock> personWithGlock) {
		List<PersonPresence> personPresence = personPresenceService.findAllByPresence(true);
		
		Map<Person, Boolean> personInPresence = personPresence.stream().collect(Collectors.toMap(PersonPresence::getPerson, PersonPresence::getPresence));
		personWithGlock.stream().forEach(p -> p.setInPresence(personInPresence.get(p.getPerson()))); 
		
		Map<Person, String> personArrivalTime = personPresence.stream().collect(Collectors.toMap(PersonPresence::getPerson, PersonPresence::getArrivalTime));
		personWithGlock.stream().forEach(p -> p.setArrivalTime(personArrivalTime.get(p.getPerson())));
		
		return personWithGlock;
	}

	private List<PersonWithGlock> convertToPersonWithGlock(List<Person> persons) {
		List<PersonWithGlock> personsGlock = new ArrayList<PersonWithGlock>();
		List<Person> events = findPersonsWithEvents();
		//@formatter:off
		persons.stream()
			.forEach(p -> personsGlock.add(
					PersonWithGlock.builder()
						.person(p)
						.glock(events.contains(p))
						.build()
						)
					);
		//@formatter:on
		return personsGlock;
	}

	private List<Person> findPersonsWithEvents() {
		List<PersonDefferedEvent> events = new ArrayList<>();
		Optional<UserSpring> userOpt = userService.getCurrentUser();
		if (userOpt.isPresent()) {
			events = personDefferedEventService.findAllByUserAndDefferedEvent(userOpt.get(), DefferedEvent.PERSON_KOMMEN);
		}
		return events.stream().filter(e -> e.getDone()).map(PersonDefferedEvent::getPerson).collect(Collectors.toList());
	}

	private Page<Person> findPaginated(int page, String findField) {
		int pageSize = 50;
		Sort sort = Sort.by("name").ascending();
		Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
		if (findField == null || findField.isBlank()) {
			return personService.findAll(pageable);
		} else {
	        return personService.findAllByNameIsLikeIgnoreCase(pageable, "%" + findField + "%");
		}
	}

	@PersonUpdatePermission
	@GetMapping(value = "/edit/{id}")
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

	@PersonReadPermission
	@GetMapping(value = "/set_glock/{id}")
	@Transactional
	public String setGlock(@PathVariable(name = "id") Long person_id, Model model) {
		Optional<UserSpring> userOpt = userService.getCurrentUser();
		if (userOpt.isPresent()) {
			personDefferedEventService.changeGlock(userOpt.get(), person_id);
		}
		return "redirect:/persons/list";
	}

	@PersonUpdatePermission
	@PostMapping(value = "/update")
	@Transactional
	public String updatePerson(@ModelAttribute("person") Person person,
			@RequestParam(value = "action", required = true) String action) {
		if (action.equals("update")) {
			personService.updatePerson(person);
		}
		return "redirect:/persons/list";
	}

	@PersonDeletePermission
	@PostMapping("/delete")
	public String deletePerson(@ModelAttribute("person") Person person) {
		this.personService.delete(person);
		return "redirect:/persons/list";
	}

	@PersonLoadPermission
	@GetMapping(value = "/load")
	public String loadPerson() {
		bcWebServicePersonLoad.loadPersonFromBC();
		return "redirect:/persons/list";
	}
	
	@PersonLoadPermission
	@GetMapping(value = "/loadBCZeitnachweis")
	public String loadBCZeitnachweis() {
		bcWebServiceLoadZeitnachweis.loadBCZeitnachweis();
		return "redirect:/persons/list";
	}

}
