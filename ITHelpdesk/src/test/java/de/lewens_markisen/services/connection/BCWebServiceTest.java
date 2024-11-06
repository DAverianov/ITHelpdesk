package de.lewens_markisen.services.connection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import de.lewens_markisen.domain.local_db.Person;
import de.lewens_markisen.domain.local_db.time_register_event.PersonInBcReport;
import de.lewens_markisen.domain.local_db.time_register_event.TimeRegisterEvent;
import de.lewens_markisen.storage.StorageService;
import de.lewens_markisen.timeReport.PeriodReport;

@ActiveProfiles("test")
@SpringBootTest()
class BCWebServiceTest {
	private final String BC_CODE = "645";
	private final String FILE_WITH_REPORT = "ZeitnachweisMitarbeiter 2409.xml";

	@Autowired
	private BCWebService bcWebService;

	@MockBean
	private StorageService storageService;

	@Test
	void readTimeRegisterEventsFromBC_whenRead_thenReceive() {
		Person person = Person.builder().name("user").bcCode(BC_CODE).build();
		PeriodReport period = PeriodReport.thisMonat();
		Optional<List<TimeRegisterEvent>> events = bcWebService.readTimeRegisterEventsFromBC(person, period);
		assertThat(events.isPresent());
		assertThat(events.get()).isNotEmpty().hasAtLeastOneElementOfType(TimeRegisterEvent.class);
	}

	@Test
	void compoundDublRecords() {
		Person person = Person.builder().name("user").bcCode(BC_CODE).build();
		LocalDate eventDate = LocalDate.of(2024, 9, 16);
		List<TimeRegisterEvent> events = new ArrayList<TimeRegisterEvent>();
		//@formatter:off
		events.add(
			TimeRegisterEvent.builder()
				.person(person)
				.eventDate(eventDate)
				.startTime("7:00")
				.endTime("")
				.build());
		events.add(
				TimeRegisterEvent.builder()
				.person(person)
				.eventDate(eventDate)
				.startTime("")
				.endTime("17:00")
				.build());
		//@formatter:on
		List<TimeRegisterEvent> eventsWithoutDoubl = bcWebService.compoundDublRecords(events);
		assertThat(eventsWithoutDoubl).isNotEmpty().hasSize(1);
		assertThat(eventsWithoutDoubl.get(0).getStartTime()).isEqualTo("7:00");
		assertThat(eventsWithoutDoubl.get(0).getEndTime()).isEqualTo("17:00");
	}

	@Test
	void readPersonsFromBC_whenQuery_thenReceive() {
		Optional<List<Person>> personsOpt = bcWebService.readPersonsFromBC();
		assertThat(personsOpt).isNotEmpty();
		assertThat(personsOpt.get()).isNotEmpty().hasAtLeastOneElementOfType(Person.class);
	}

	@Test
	void loadBCZeitnachweis_whenLoad_thenReceive() throws IOException {
	    String path = "/src/test/resources";
	    
	    Path originalPath = new File("."+path+"/testFiles", FILE_WITH_REPORT).toPath();
	    Path copied = Paths.get("src/test/resources/"+FILE_WITH_REPORT);
	    Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);

	    File file = new File("."+path, FILE_WITH_REPORT);

		given(this.storageService.load(anyString())).willReturn(file.toPath());

		List<PersonInBcReport> personsInBcRep = bcWebService.loadBCZeitnachweis();

//		assertThat(personsInBcRep).isNotEmpty();
//		PersonInBcReport personInBcRep = personsInBcRep.get(0);
//		assertThat(personInBcRep.getAttribute().get("AZ_Person__Code")).isNotNull();
//		assertThat(personInBcRep.getAttribute().get("AZ_Person__Name")).isNotNull();
		
	}

}
