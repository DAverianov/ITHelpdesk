package de.lewens_markisen.web.controllers.Person;

import java.util.ArrayList;
import java.util.List;

import de.lewens_markisen.domain.Person;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Persons {

	private List<Person> persons;

	@XmlElement
	public List<Person> getPersonList() {
		if (persons == null) {
			persons = new ArrayList<>();
		}
		return persons;
	}

}
