package de.lewens_markisen.timeReport;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
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
	private List<TimeReportRecord> recordsWithGroups;
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
	
	public void createRecordsWithGroups() {
		this.recordsWithGroups = new ArrayList<TimeReportRecord>();
		this.timeRecords.stream().forEach(tr -> this.recordsWithGroups.add(new TimeReportRecord(tr)));
	}

	public void createGroup(int groupNummer, Function<TimeReportRecord, String> getGroupName) {
		String groupName = "";
		
		TimeReportRecord tr;
		int size = this.recordsWithGroups.size();
		for (int i=0; i<size; i++) {
			tr = this.recordsWithGroups.get(i);
			if (getGroupName.apply(tr).equals(groupName)) {
				continue;
			}
			else {
				groupName = getGroupName.apply(tr);
				//@formatter:off
				this.recordsWithGroups.add(TimeReportRecord.builder()
						 .name(groupName)
						 .eventDate(tr.getEventDate())
						 .group(groupNummer)
						 .build());
				//@formatter:on
			}
		}
		this.recordsWithGroups.stream().filter(w -> w.getGroup()==groupNummer).forEach(w -> countGroupSumm(w, getGroupName));
		Collections.sort(this.recordsWithGroups);
	}

	private void countGroupSumm(TimeReportRecord groupRecord, Function<TimeReportRecord, String> getGroupName) {
		String group = getGroupName.apply(groupRecord);
		this.recordsWithGroups.stream().filter(w -> getGroupName.apply(w).equals(group) && w.getGroup()==0).forEach(w -> groupRecord.addSumm(w));
	}
	
	public String header() {
		return ""+getPerson().getName()+" "+getPeriod().getPeriod();
	}
	public String headerService() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return ""+LocalDateTime.now().format(formatter);
	}
	
}
