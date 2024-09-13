package de.lewens_markisen.person;

import java.util.ArrayList;
import java.util.List;

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
