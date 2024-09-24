package de.lewens_markisen.timeReport;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

	public void createReportRecords() {
		this.recordsWithGroups = new ArrayList<TimeReportRecord>();
		this.timeRecords.stream().forEach(tr -> this.recordsWithGroups.add(new TimeReportRecord(tr)));
	}

	public void createGroup(int groupNummer, Function<TimeReportRecord, String> getGroupName,
			Function<TimeReportRecord, LocalDate> periodStart) {
		addGroup(groupNummer, getGroupName, periodStart);
		this.recordsWithGroups.stream().filter(w -> w.getGroup() == groupNummer)
				.forEach(w -> countGroupSumm(w, getGroupName));
		Collections.sort(this.recordsWithGroups);
	}

	private void addGroup(int groupNummer, Function<TimeReportRecord, String> getGroupName,
			Function<TimeReportRecord, LocalDate> periodStart) {
		//@formatter:off
		for (String groupName : getDiversyOfGroups(getGroupName)) {
			this.recordsWithGroups.stream()
				.filter(r -> getGroupName.apply(r).equals(groupName) && r.getGroup() == 0)
				.findFirst().stream()
					.forEach(firstR -> 
						this.recordsWithGroups.add(TimeReportRecord.builder()
							.name(groupName)
							.eventDate(periodStart.apply(firstR))
							.group(groupNummer)
							.build())
						);
		}
		//@formatter:on
	}

	private Set<String> getDiversyOfGroups(Function<TimeReportRecord, String> getGroupName) {
		//@formatter:off
		return this.recordsWithGroups.stream()
				.filter(r -> r.getGroup() == 0)
				.map(getGroupName)
				.collect(Collectors.toSet());
		//@formatter:on
	}

	private void countGroupSumm(TimeReportRecord groupRecord, Function<TimeReportRecord, String> getGroupName) {
		String group = getGroupName.apply(groupRecord);
		this.recordsWithGroups.stream().filter(w -> getGroupName.apply(w).equals(group) && w.getGroup() == 0)
				.forEach(w -> groupRecord.addSumm(w));
	}

	public String header() {
		return "" + getPerson().getName() + " " + getPeriod().getPeriod();
	}

	public String headerService() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		return "" + LocalDateTime.now().format(formatter);
	}

	public LocalDate startGroup(LocalDate date, Function<LocalDate, LocalDate> findStartOfGroup) {
		LocalDate periodStart = this.period.getStart();
		LocalDate groupStart = findStartOfGroup.apply(date);
		if (periodStart.isBefore(groupStart)) {
			return groupStart;
		} else {
			return periodStart;
		}
	}

}
