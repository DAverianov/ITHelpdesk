package de.lewens_markisen.timeReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	private String comment;
	private PeriodReport period;
	
	public List<TimeRecordReport> getWeeks() {
		List<TimeRecordReport> weeks = new ArrayList<TimeRecordReport>();
		for (TimeRegisterEvent tr: timeRecords) {
			addWeek(weeks, tr);
		}
		return weeks;
	}

	private void addWeek(List<TimeRecordReport> weeks, TimeRegisterEvent tr) {
		String yearWeek = tr.getYearWeek();
		//@formatter:off
		Optional<TimeRecordReport> week = weeks.stream()
			.filter(r-> r.getName().equals(yearWeek))
			.findFirst();
		if (week.isPresent()) {
			week.get().addRecord(tr);
		}
		else {
			TimeRecordReport timeRecordWeek = TimeRecordReport.builder()
				.name(yearWeek).build();
			timeRecordWeek.addRecord(tr);
			weeks.add(timeRecordWeek);
		}
		//@formatter:on
	}
	
}
