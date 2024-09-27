package de.lewens_markisen.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StringUtilsLSSTest {

	@Test
	void replaceUmlauts_whenCall_thenGet() {
		String test = "AaÄäOoÖöUuÜü test";
		assertEquals("AaAaOoOoUuUu test", StringUtilsLSS.replaceUmlauts(test));
	}

}
