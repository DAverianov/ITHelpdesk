package de.lewens_markisen.utils;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Chars;

public class TimeUtils {
	public static final int MINUTES_PER_HOUR = 60;
	public static final int SECONDS_PER_MINUTE = 60;
	public static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

	public static String secondsToHourMinutes(long seconds, boolean inDecimal) {
		if (seconds > 0) {
			if (inDecimal) {
				return "" + String.format("%.2f", (double) seconds / SECONDS_PER_HOUR);
			} else {
				int hours = (int) (seconds / SECONDS_PER_HOUR);
				int minutes = (int) seconds % SECONDS_PER_HOUR / 60;
				return "" + hours + ":" + String.format("%02d", minutes);
			}
		} else {
			return "--";
		}
	}

	public static String getYearWeek(LocalDate eventDate) {
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
		return "" + eventDate.getYear() + " Woche " + eventDate.get(woy);
	}

	public static String getYearMonat(LocalDate eventDate) {
		return "" + eventDate.getYear() + " " + eventDate.getMonthValue();
	}

	public static Integer convertTimeToInt(String stringTime) {
		String source = StringUtils.trim(stringTime);
		char delimiter = ':';
		String hour = (source.indexOf(delimiter) != -1) ? source.substring(0, source.indexOf(delimiter)) : "0";
		String minuts = (source.indexOf(delimiter) != -1) ? source.substring(source.indexOf(delimiter)+1, source.length()) : "0";
		
		return Integer.valueOf(hour)*100 + Integer.valueOf(minuts);
	}
	public static Integer convertTimeToMinuten(String stringTime) {
		String source = StringUtils.trim(stringTime);
		char delimiter = ':';
		String hour = (source.indexOf(delimiter) != -1) ? source.substring(0, source.indexOf(delimiter)) : "0";
		String minuts = (source.indexOf(delimiter) != -1) ? source.substring(source.indexOf(delimiter)+1, source.length()) : "0";
		
		return Integer.valueOf(hour)*60 + Integer.valueOf(minuts);
	}

	public static String getHoures(String timeStr) {
		return StringUtils.deleteWhitespace(timeStr.substring(0, timeStr.indexOf(":")));
	}

}
