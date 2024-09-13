package de.lewens_markisen.timeReport;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeWeekDays extends TimeRecordReport{

	public TimeWeekDays(String name, Integer mo, Integer tu, Integer we, Integer th, Integer fr, Integer sa,
			Integer so) {
		super(name, mo, tu, we, th, fr, sa, so);
	}
	private Integer weekNummer;

}
