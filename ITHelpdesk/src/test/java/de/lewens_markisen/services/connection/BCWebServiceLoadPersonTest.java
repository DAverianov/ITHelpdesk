package de.lewens_markisen.services.connection;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import de.lewens_markisen.domain.local_db.person.Person;

@ActiveProfiles("test")
@SpringBootTest()
class BCWebServiceLoadPersonTest {

	@Autowired
	private BCWebServiceLoadPerson bcWebServiceLoadPerson;

	@Test
	void readPersonsFromBC_whenQuery_thenReceive() {
		Optional<List<Person>> personsOpt = bcWebServiceLoadPerson.readPersonsFromBC();
		assertThat(personsOpt).isNotEmpty();
		assertThat(personsOpt.get()).isNotEmpty().hasAtLeastOneElementOfType(Person.class);
	}

}
