package de.lewens_markisen.person;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.lewens_markisen.domain.local_db.Person;

class PersonTest {

	@Test
	void build_whenCreate_thenNameWithoutSpace() {
		Person person = Person.builder().name("John Rockfeller").bcCode("1").build();
		assertEquals(person.getNameForSearch(), "johnrockfeller");
	}

}
