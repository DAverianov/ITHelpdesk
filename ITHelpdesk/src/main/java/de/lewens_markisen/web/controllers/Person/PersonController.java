package de.lewens_markisen.web.controllers.Person;

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
import de.lewens_markisen.domain.Person;
import de.lewens_markisen.repositories.PersonRepository;
import de.lewens_markisen.services.PersonService;

@Controller
@RequestMapping("/persons")
public class PersonController {

    public static Comparator<BaseEntity> COMPARATOR_BY_ID = Comparator.comparing(BaseEntity::getId);
    public static Comparator<Person> COMPARATOR_BY_NAME = Comparator.comparing(Person::getName);

    private final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }
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
    public ModelAndView showEditPersonForm(@PathVariable(name = "id") Long id) {
        ModelAndView modelAndView = new ModelAndView("persons/personEdit");
        System.out.println("edit!");
        Optional<Person> personOpt = personService.findById(id);
        if (personOpt.isPresent()) {
	        modelAndView.addObject("person", personOpt.get());
        }
        else {
        	modelAndView.setViewName("error");
         }
        return modelAndView;
   }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateStudent(@ModelAttribute("person") Person person, @RequestParam(value="action", required=true) String action) {
        if (action.equals("update")) {
            personService.updatePerson(person);
        }
        return "redirect:/";
    }

}