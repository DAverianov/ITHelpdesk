package de.lewens_markisen.timeReport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;

import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.security.UserSpringService;
import de.lewens_markisen.timeReport.TimeReport;
import de.lewens_markisen.timeReport.TimeReportService;
import de.lewens_markisen.web.controllers.BaseIT;

@SpringBootTest
//@Disabled
class TimeReportServiceTest{
	private final String BC_CODE = "1071";
	private final String USER = "test";
	
	@Autowired
	private TimeReportService timeReportService;

	@Test
	void createReport_whenCreate_then() {
		Optional<TimeReport> timeReport = timeReportService.createReport(BC_CODE);
		assertThat(timeReport).isNotEmpty();
	}

	@Test
	void getWeeks_whenQuery_thenCompound() {
		Optional<TimeReport> timeReport = timeReportService.createReport(BC_CODE);
		assertThat(timeReport).isNotEmpty();
	}
	

	@Rollback
	@Test
	void getUserBcCode_whenGet_thenBekomm() {
		Optional<String> bcCodeOpt = timeReportService.getUserBcCode(USER);
		assertThat(bcCodeOpt).isNotEmpty();
		assertEquals(bcCodeOpt.get(), BC_CODE);
	}
}
