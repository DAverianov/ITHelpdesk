package de.lewens_markisen.utils;

public class StringUtilsLSS {
	public static String replaceUmlauts(String source) {
		//@formatter:off
		return source
				.replaceAll("Ä", "A")
				.replaceAll("ä", "a")
				.replaceAll("Ö", "O")
				.replaceAll("ö", "o")
				.replaceAll("Ü", "U")
				.replaceAll("ü", "u");
		//@formatter:on
	}
}
