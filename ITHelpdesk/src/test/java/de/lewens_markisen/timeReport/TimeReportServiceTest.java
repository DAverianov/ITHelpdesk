package de.lewens_markisen.timeReport;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import de.lewens_markisen.person.Person;

@ActiveProfiles("test")
@SpringBootTest()
class TimeReportServiceTest {
	private final String BC_CODE = "645";
	@Autowired
	private TimeReportService timeReportService;

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
		List<TimeRecordReport> weeks= timeReport.get().getWeeks();
		assertThat(weeks).isNotEmpty();
	}
}
