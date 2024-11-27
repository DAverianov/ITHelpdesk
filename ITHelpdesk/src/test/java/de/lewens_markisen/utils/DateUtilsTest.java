package de.lewens_markisen.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class DateUtilsTest {

	@Test
	void startMonat_return01() {
		LocalDate dat = LocalDate.of(2024, 10, 1);
		assertEquals(LocalDate.of(2024, 10, 1), DateUtils.startMonat(dat));
		
		dat = LocalDate.of(2024, 10, 14);
		assertEquals(LocalDate.of(2024, 10, 1), DateUtils.startMonat(dat));
	}
	
	@Test
	void endMonat_returnLastDate() {
		LocalDate dat = LocalDate.of(2024, 10, 1);
		assertEquals(LocalDate.of(2024, 10, 31), DateUtils.endMonat(dat));
		
		dat = LocalDate.of(2024, 10, 14);
		assertEquals(LocalDate.of(2024, 10, 31), DateUtils.endMonat(dat));

		dat = LocalDate.of(2024, 02, 28);
		assertEquals(LocalDate.of(2024, 02, 29), DateUtils.endMonat(dat));
	}
	
	@Test
	void startWeekInMonat_returnStartWeek() {
		LocalDate dat = LocalDate.of(2024, 10, 16);
		assertEquals(LocalDate.of(2024, 10, 14), DateUtils.startWeekInMonat(dat));
	}

}
