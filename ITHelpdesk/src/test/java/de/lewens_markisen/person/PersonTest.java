package de.lewens_markisen.person;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;

class PersonTest {

	@Test
	void build_whenCreate_thenNameWithoutSpace() {
		Person person = Person.builder().name("John Rockfeller").bcCode("1").build();
		assertEquals(person.getNameWithoutSpace(), "JohnRockfeller");
	}

}
