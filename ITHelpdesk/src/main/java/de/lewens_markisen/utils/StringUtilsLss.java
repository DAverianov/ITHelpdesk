package de.lewens_markisen.utils;

import org.apache.commons.lang3.StringUtils;

public class StringUtilsLss {
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
		return StringUtilsLss.replaceUmlauts(StringUtils.lowerCase(StringUtils.deleteWhitespace(username)));
	}

	public static String cut(String text, int length) {
		int lengthIs = text.length();
		if (lengthIs > length) {
			return StringUtils.left(text, length)+"..";
		}
		else {
			return text;
		}
	}
}
