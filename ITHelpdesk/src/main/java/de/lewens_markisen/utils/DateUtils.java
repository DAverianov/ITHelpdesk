package de.lewens_markisen.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

	public static List<LocalDate> getDateInMonth(LocalDate dayOfMonth) {
		List<LocalDate> dates = new ArrayList<LocalDate>();
		int year = dayOfMonth.getYear();
		int month = dayOfMonth.getMonthValue();
		for (int i = 1; i<=dayOfMonth.lengthOfMonth(); i++) {
			dates.add(LocalDate.of(year, month, i));
		}
		return dates;
	}

}
