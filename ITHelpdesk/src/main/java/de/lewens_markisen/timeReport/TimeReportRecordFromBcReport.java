package de.lewens_markisen.timeReport;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TimeReportRecordFromBcReport {
	private String month;
	private String calendarArt;
	private String timeSaldo;
	private Integer timeSaldoSign;
	private String urlaubSaldo;
	private String sollStunde;

}
