package de.lewens_markisen.timeRegisterEvent;

import static org.junit.Assert.assertEquals;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest()
class TimeRegisterEventTest {

	@Test
	void toStringReport_whenIsDate_thenReturn() {
		//@formatter:off
		TimeRegisterEvent tr = TimeRegisterEvent.builder()
				.eventDate(LocalDate.of(2024, 9, 13))
				.startTime("07:00")
				.endTime("10:20")
				.build();
		//@formatter:on
		assertEquals("2024-09-13 07:00 - 10:20", tr.toStringReport());
		tr.setEndTime("9:00");
		assertEquals("2024-09-13 07:00 - 9:00", tr.toStringReport());
	}
	
	@Test
	void getMo_whenIsDate_thenReturnMontag() {
		//@formatter:off
		TimeRegisterEvent tr = TimeRegisterEvent.builder()
				.eventDate(LocalDate.of(2024, 9, 9))
				.startTime("7:00")
				.endTime("10:20")
				.build();
		//@formatter:on
		assertEquals("3:20", tr.getMo());
		tr.setEndTime("");
		assertEquals("", tr.getMo());
		tr.setEndTime(null);
		assertEquals("", tr.getMo());
		tr.setEndTime("6:00");
		assertEquals("--", tr.getMo());
	}

}
