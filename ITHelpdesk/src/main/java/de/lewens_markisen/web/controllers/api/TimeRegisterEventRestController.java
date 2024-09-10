package de.lewens_markisen.web.controllers.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.lewens_markisen.domain.Person;
import de.lewens_markisen.services.PersonService;
import de.lewens_markisen.services.TimeRegisterEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/zeiterfassungsystem")
@RestController
public class TimeRegisterEventRestController {

	@Autowired
	TimeRegisterEventService timeRegisterEventService;
	@Autowired
	PersonService personService;

	@GetMapping()
	public ResponseEntity<String> listTimeEvents(@RequestParam(value = "Person", required = true) String person) {

		log.debug("Listing Time register events");
		String antwort = "";
		Optional<Person> personOpt = personService.findByBcCode(person);
		if (personOpt.isPresent()) {
			Optional<String> timeRecords = timeRegisterEventService.readEventsProPerson(personOpt.get());
			if (timeRecords.isPresent()) {
				antwort = "Hello "
						+ personOpt.get().getName() 
						+ System.lineSeparator()
						+ timeRecords.get().toString();
			}
			else {
				antwort = "Persons Zeit "+person+" sind nicht gefunden!";
			}
		} else {
			log.error("Query for Person "+person+" isnt successfull!");
			antwort = "Person "+person+" ist nicht gefunden!";
		}
		return new ResponseEntity<>(antwort, HttpStatus.OK);
	}

}
