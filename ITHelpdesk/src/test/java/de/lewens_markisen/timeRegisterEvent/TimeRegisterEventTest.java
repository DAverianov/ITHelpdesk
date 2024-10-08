package de.lewens_markisen.timeRegisterEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import de.lewens_markisen.domain.localDb.TimeRegisterEvent;

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
		assertEquals("2024-09-13  /07:00 - 10:20/ - 0:15 = 3:05", tr.toStringReport());
		tr.setEndTime("9:00");
		assertEquals("2024-09-13  /07:00 - 9:00/ - 0:15 = 1:45", tr.toStringReport());
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
		assertEquals("2,58", tr.getMoDecimal());
		tr.setEndTime("");
		assertEquals(0l, tr.getMo());
		tr.setEndTime(null);
		assertEquals(0l, tr.getMo());
		tr.setEndTime("6:00");
		assertEquals(-6300l, tr.getMo());
	}
	
//	@Test
//	void timeOfWork() {
//		TimeRegisterEvent tr = TimeRegisterEvent.builder()
//				.eventDate(LocalDate.of(2024, 9, 9))
//				.startTime("7:00")
//				.endTime("7:30")
//				.build();
//		String time = tr.timeOfWork(true);
//		assertEquals("0,50", time);
//		time = tr.timeOfWork(false);
//		assertEquals("0:30", time);
//	}

}
