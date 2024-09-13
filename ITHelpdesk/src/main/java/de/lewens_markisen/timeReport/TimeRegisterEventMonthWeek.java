package de.lewens_markisen.timeReport;

import de.lewens_markisen.timeRegisterEvent.TimeRegisterEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeRegisterEventMonthWeek extends TimeRegisterEvent{
	private TimeMonthWeeks month;
	private TimeWeekDays week;
}
