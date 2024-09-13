package de.lewens_markisen.timeReport;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import de.lewens_markisen.person.Person;
import de.lewens_markisen.timeRegisterEvent.TimeRegisterEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Component
public class TimeReport {

	@Builder
	public TimeReport(Person person, List<TimeRegisterEvent> timeRecords, String comment, PeriodReport period) {
		super();
		this.person = person;
		this.timeRecords = timeRecords;
		this.comment = comment;
		this.period = period;
	}
	private Person person;
	private List<TimeRegisterEvent> timeRecords;
	private List<TimeRegisterMonths> timeMonths;
	private String comment;
	private PeriodReport period;
}
