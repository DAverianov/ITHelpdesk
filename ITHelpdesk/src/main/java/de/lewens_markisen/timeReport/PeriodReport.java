package de.lewens_markisen.timeReport;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeriodReport {

	@Builder
	public PeriodReport(LocalDate start, LocalDate end) {
		super();
		this.start = start;
		this.end = end;
	}
	
	private LocalDate start;
	private LocalDate end;
	
	public static PeriodReport PeriodReportMonth() {
		LocalDate now = LocalDate.now();
		return PeriodReport.builder()
				.start(now.withDayOfMonth(1))
				.end(now.withDayOfMonth(now.lengthOfMonth()))
				.build();
	}
	public static PeriodReport PeriodReportMonth(LocalDate now) {
		return PeriodReport.builder()
				.start(now.withDayOfMonth(1))
				.end(now.withDayOfMonth(now.lengthOfMonth()))
				.build();
	}
	
	public String getPeriod() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		return "("+getStart().format(formatter)+" - "+getEnd().format(formatter)+")";
	}
}
