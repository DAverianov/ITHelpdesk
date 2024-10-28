package de.lewens_markisen.utils;

import org.apache.commons.lang3.StringUtils;

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

	public static String convertNameToLowCase(String username) {
		return StringUtilsLSS.replaceUmlauts(StringUtils.lowerCase(StringUtils.deleteWhitespace(username)));
	}

	public static String getHoures(String timeStr) {
		return StringUtils.deleteWhitespace(timeStr.substring(0, timeStr.indexOf(":")));
	}
}
