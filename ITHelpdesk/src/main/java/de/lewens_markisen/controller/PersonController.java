package de.lewens_markisen.controller;

import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.lewens_markisen.domain.BaseEntity;
import de.lewens_markisen.domain.Person;
import de.lewens_markisen.services.PersonService;

@Controller
@RequestMapping("/persons")
public class PersonController {

    public static Comparator<BaseEntity> COMPARATOR_BY_ID = Comparator.comparing(BaseEntity::getId);
    public static Comparator<BaseEntity> COMPARATOR_BY_NAME = Comparator.comparing(BaseEntity::toString);

    private final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }
    @GetMapping(path = "/list")
    public String list(Model uiModel) {
        Iterable<Person> persons = personService.findAll();
        uiModel.addAttribute("persons", persons);

        return "persons/list";
    }

}
