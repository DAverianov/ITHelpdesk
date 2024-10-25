package de.lewens_markisen.timeRegisterEvent;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.lewens_markisen.domain.local_db.Person;
import de.lewens_markisen.domain.local_db.time_register_event.PersonInBcReport;
import de.lewens_markisen.person.PersonService;

@SpringBootTest
class PersonInBcReportServiceImplTest {
	@Autowired
	private PersonInBcReportService personInBcReportService;
	@Autowired
	private PersonService personService;
	
	@Test
	void findPerson() {
		
		Optional<Person> personOpt = personService.findByBcCode("1071");
		Optional<PersonInBcReport> persInBcRep = personInBcReportService.findByPersonAndMonth(personOpt.get(), LocalDate.of(2024, 9, 1));
		assertThat(persInBcRep).isNotEmpty();
		//@formatter:off
		persInBcRep.get().getSaldo().getSaldoList().stream().forEach(System.out::println);
		String urlaub = persInBcRep.get().getSaldo().getSaldoList()
			.stream()
			.filter(s -> "Urlaubssaldo in Tagen".equals(s.getKSaldo_Bezeichnung()))
			.findFirst()
			.map(u -> u.getGtisBASaldoEndeAktPer()).orElse("0");
		System.out.println("Urlaub saldo: "+urlaub);
		//@formatter:on
		
	}

}
