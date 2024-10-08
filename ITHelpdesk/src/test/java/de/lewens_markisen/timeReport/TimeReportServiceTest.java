package de.lewens_markisen.timeReport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import de.lewens_markisen.config.SecurityConfiguration;
import de.lewens_markisen.domain.localDb.Person;
import de.lewens_markisen.person.PersonService;

@ActiveProfiles("test")
@SpringBootTest()
@Disabled
class TimeReportServiceTest {
	private final String BC_CODE = "1071";
	@Autowired
	private TimeReportService timeReportService;
	@Autowired
	private PersonService personService;
	@Autowired
	SecurityConfiguration provider;

	@Test
	void createReport_whenCreate_then() {
		Person person = Person.builder().name("user").bcCode(BC_CODE).build();
		Optional<TimeReport> timeReport = timeReportService.createReport(BC_CODE);
		assertThat(timeReport).isNotEmpty();
	}

	@Test
	void getWeeks_whenQuery_thenCompound() {
		Person person = Person.builder().name("user").bcCode(BC_CODE).build();
		Optional<TimeReport> timeReport = timeReportService.createReport(BC_CODE);
		assertThat(timeReport).isNotEmpty();
////		List<TimeReportGroupRecords> weeks= timeReport.get().getWeeks();
//		assertThat(weeks).isNotEmpty();
//		assertThat(weeks.get(0).getElements()).isNotEmpty();
//		for (TimeReportGroupRecords gr: weeks) {
//			assertThat(gr.getElements()).isNotEmpty();
//		}
	}
	

//	@Test
	void getUserBcCode_whenGet_thenBekomm() {
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("it-team", "1#01");
		personService.findOrCreate(BC_CODE, "it-team");
		auth = (UsernamePasswordAuthenticationToken) provider.authenticate(auth);
		Optional<String> bcCodeOpt = timeReportService.getUserBcCode();
		assertThat(bcCodeOpt).isNotEmpty();
		assertEquals(bcCodeOpt.get(), BC_CODE);
	}

//	@Test
	void createWeek_whenCreat_thenBekomm() {
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("it-team", "1#01");
		personService.findOrCreate(BC_CODE, "it-team");
		auth = (UsernamePasswordAuthenticationToken) provider.authenticate(auth);
		Optional<String> bcCodeOpt = timeReportService.getUserBcCode();
		assertThat(bcCodeOpt).isNotEmpty();
		assertEquals(bcCodeOpt.get(), BC_CODE);
	}

}
