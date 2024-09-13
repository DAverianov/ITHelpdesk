package de.lewens_markisen.timeReport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeriodReport {

	@Builder
	public PeriodReport(LocalDateTime start, LocalDateTime end) {
		super();
		this.start = start;
		this.end = end;
	}
	
	private LocalDateTime start;
	private LocalDateTime end;
	
	public static PeriodReport PeriodReportMonth() {
		LocalDate now = LocalDate.now();
		return PeriodReport.builder()
				.start(LocalDateTime.of(now.withDayOfMonth(1), LocalTime.MIN))
				.end(LocalDateTime.of(now.withDayOfMonth(now.lengthOfMonth()), LocalTime.MAX))
				.build();
	}
	public static PeriodReport PeriodReportMonth(LocalDate now) {
		return PeriodReport.builder()
				.start(LocalDateTime.of(now.withDayOfMonth(1), LocalTime.MIN))
				.end(LocalDateTime.of(now.withDayOfMonth(now.lengthOfMonth()), LocalTime.MAX))
				.build();
	}
}
