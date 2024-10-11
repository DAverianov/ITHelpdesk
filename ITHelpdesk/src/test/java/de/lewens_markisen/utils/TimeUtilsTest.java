package de.lewens_markisen.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TimeUtilsTest {

	@Test
	void convertTimeToInt() {
		String time = "7:30";
		assertEquals(730, TimeUtils.convertTimeToInt(time));
	}

}
