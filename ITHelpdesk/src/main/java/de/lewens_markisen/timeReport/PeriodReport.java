package de.lewens_markisen.timeReport;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import de.lewens_markisen.utils.DateUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
public class PeriodReport {

	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate start;
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate end;

	public static PeriodReport periodReportMonth() {
		LocalDate now = LocalDate.now();
		//@formatter:off
		return PeriodReport.builder()
				.start(now.withDayOfMonth(1))
				.end(now.withDayOfMonth(now.lengthOfMonth()))
				.build();
		//@formatter:on
	}

	public static PeriodReport periodReportMonth(LocalDate now) {
		//@formatter:off
		return PeriodReport.builder()
				.start(now.withDayOfMonth(1))
				.end(now.withDayOfMonth(now.lengthOfMonth()))
				.build();
		//@formatter:on
	}

	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		DateTimeFormatter formatterMonat = DateTimeFormatter.ofPattern("MMMM.yyyy");
		if (getStart()==null || getEnd()==null) {
			return "(" + getStart().format(formatter) + " - " + getEnd().format(formatter) + ")";
		}
		if (getStart().equals(getStart().withDayOfMonth(1)) // Month
				&& getEnd().equals(getEnd().withDayOfMonth(getEnd().lengthOfMonth()))) {
			return getStart().format(formatterMonat);
		} else {
			return "(" + getStart().format(formatter) + " - " + getEnd().format(formatter) + ")";
		}
	}
	
	public static String formatMonth(LocalDate date) {
		DateTimeFormatter formatterMonat = DateTimeFormatter.ofPattern("MMMM.yyyy");
		return date.format(formatterMonat);
	}

	public static PeriodReport thisMonat() {
		return PeriodReport.builder().start(DateUtils.startMonat(LocalDate.now())).end(LocalDate.now()).build();
	}
}
