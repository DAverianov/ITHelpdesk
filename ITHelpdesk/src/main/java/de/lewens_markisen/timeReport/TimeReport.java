package de.lewens_markisen.timeReport;

import java.util.ArrayList;
import java.util.Comparator;
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

	private Person person;
	private List<TimeRegisterEvent> timeRecords;
	private List<TimeReportRecord> weeks;
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
	
	public void createWeeks() {
		String yearWeek = "";
		this.weeks = new ArrayList<TimeReportRecord>();
		timeRecords.stream().forEach(tr -> this.weeks.add(new TimeReportRecord(tr)));
		
		TimeReportRecord tr;
		int size = this.weeks.size();
		for (int i=0; i<size; i++) {
			tr = this.weeks.get(i);
			if (tr.getYearWeek().equals(yearWeek)) {
				continue;
			}
			else {
				yearWeek = tr.getYearWeek();
				//@formatter:off
				this.weeks.add(TimeReportRecord.builder()
						 .name(yearWeek)
						 .eventDate(tr.getEventDate())
						 .group(1)
						 .build());
				//@formatter:on
			}
		}
		this.weeks.sort(Comparator.comparing(TimeReportRecord::getEventDate).thenComparing(TimeReportRecord::getGroup));
	}

//	private void addWeek(List<TimeReportGroupRecords> weeks, TimeRegisterEvent tr) {
//		String yearWeek = TimeUtils.getYearWeek(tr.getEventDate());
//		//@formatter:off
//		Optional<TimeReportGroupRecords> week = weeks.stream()
//			.filter(r-> r.getName().equals(yearWeek))
//			.findFirst();
//		if (week.isPresent()) {
//			week.get().addElement(new TimeReportRecord(tr));
//		}
//		else {
//			TimeReportGroupRecords timeRecordWeek = TimeReportGroupRecords.builder()
//				.name(yearWeek).build();
//			timeRecordWeek.addElement(new TimeReportRecord(tr));
//			weeks.add(timeRecordWeek);
//		}
//		//@formatter:on
//	}
	
}
