package de.lewens_markisen.timeReport;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import de.lewens_markisen.person.Person;
import de.lewens_markisen.timeRegisterEvent.TimeRegisterEvent;
import de.lewens_markisen.utils.TimeUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Component
public class TimeReport {

	private Person person;
	private List<TimeRegisterEvent> timeRecords;
	private String comment;
	private PeriodReport period;

	@Builder
	public TimeReport(Person person, List<TimeRegisterEvent> timeRecords, String comment, PeriodReport period) {
		super();
		this.person = person;
		this.timeRecords = timeRecords;
		this.comment = comment;
		this.period = period;
	}
	
	public List<TimeReportRecord> getWeeks() {
		List<TimeReportRecord> weeks = new ArrayList<TimeReportRecord>();
		//		Set<String> weeks = new HashSet<String>();
//		timeRecords.stream()
//        	.map(tr -> TimeUtils.getYearWeek(tr.getEventDate())
//        	.collect(Collectors.toCollection(HashSet::new));
//		
//		List<TimeReportGroupRecords> weeks = new ArrayList<TimeReportGroupRecords>();
//		for (TimeRegisterEvent tr: timeRecords) {
//			addWeek(weeks, tr);
//		}
		return weeks;
	}

	private void addWeek(List<TimeReportGroupRecords> weeks, TimeRegisterEvent tr) {
		String yearWeek = TimeUtils.getYearWeek(tr.getEventDate());
		//@formatter:off
		Optional<TimeReportGroupRecords> week = weeks.stream()
			.filter(r-> r.getName().equals(yearWeek))
			.findFirst();
		if (week.isPresent()) {
			week.get().addElement(new TimeReportRecord(tr));
		}
		else {
			TimeReportGroupRecords timeRecordWeek = TimeReportGroupRecords.builder()
				.name(yearWeek).build();
			timeRecordWeek.addElement(new TimeReportRecord(tr));
			weeks.add(timeRecordWeek);
		}
		//@formatter:on
	}
	
}
