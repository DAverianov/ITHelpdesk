package de.lewens_markisen.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
	
	public static LocalDate startMonat(LocalDate dat) {
		return LocalDate.of(dat.getYear(), dat.getMonth(), 1);
	}
	
	public static LocalDate endMonat(LocalDate dat) {
		return dat.withDayOfMonth(dat.lengthOfMonth());
	}
	
	public static String formattDDMMYYY(LocalDate dat) {
		if (dat == null) {
			return "";
		}
		else {
			return dat.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		}
	}

	public static LocalDate startWeekInMonat(LocalDate date) {
		LocalDate startWeek = date.with(DayOfWeek.MONDAY);
		LocalDate startMonth = startMonat(date);
		if (startWeek.compareTo(startMonth)>0) {
			return startWeek;
		}
		else {
			return startMonth;
		}
	}

	public static LocalDate readDateFromString(String reportMonth, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return LocalDate.parse(reportMonth.substring(0, 8), formatter);
	}

}
