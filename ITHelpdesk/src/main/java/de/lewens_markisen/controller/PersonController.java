package de.lewens_markisen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import de.lewens_markisen.services.PersonService;

@Controller
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @RequestMapping("/persons")
    public String getAuthors(Model model) {
        model.addAttribute("persons", personService.findAll());
        return "persons";
    }

}
