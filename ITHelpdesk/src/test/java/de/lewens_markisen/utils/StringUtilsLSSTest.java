package de.lewens_markisen.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StringUtilsLSSTest {

	@Test
	public void replaceUmlauts_whenCall_thenGet() {
		String test = "AaÄäOoÖöUuÜü test";
		assertEquals("AaAaOoOoUuUu test", StringUtilsLss.replaceUmlauts(test));
	}
	
	@Test
	public void cut_whenCut_thenOk() {
		String test = "Aa;alksdjf ;lasjdflksjfkjs";
		assertEquals("Aa..", StringUtilsLss.cut(test, 2));
		assertEquals(test, StringUtilsLss.cut(test, 100));
	}

}
