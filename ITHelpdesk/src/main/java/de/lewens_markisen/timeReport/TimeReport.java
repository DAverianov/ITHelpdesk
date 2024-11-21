package de.lewens_markisen.timeReport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import de.lewens_markisen.bc_reports.BcReportZeitNachweisDateDescription;
import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.time_register_event.DayArt;
import de.lewens_markisen.domain.local_db.time_register_event.PersonInBcReport;
import de.lewens_markisen.domain.local_db.time_register_event.TimeRegisterEvent;
import de.lewens_markisen.timeRegisterEvent.DayArtService;
import de.lewens_markisen.utils.DateUtils;
import de.lewens_markisen.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TimeReport {

	private Person person;
	@DateTimeFormat(iso = ISO.DATE)
	private PeriodReport period;
	private String header;
	private List<TimeRegisterEvent> timeRecords;
	private List<TimeReportRecord> recordsWithGroups;
	private String comment;
	private Optional<PersonInBcReport> personInBcReportLastMonat;
	private Optional<PersonInBcReport> personInBcReport;

	public void createReportRecords() {
		this.recordsWithGroups = new ArrayList<TimeReportRecord>();
		//@formatter:off
		this.timeRecords.stream()
			.forEach(tr -> this.recordsWithGroups.add(
				TimeReportRecord.builder()
					.timeRegisterEvent(tr)
					.eventDate(tr.getEventDate())
					.group(0)
					.build()));
		//@formatter:on
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
			for (TimeReportRecord tr: this.recordsWithGroups) {
				if (getGroupName.apply(tr).equals(groupName) && tr.getGroup() == 0) {
					this.recordsWithGroups.add(
							TimeReportRecord.builder()
								.name(groupName)
								.eventDate(periodStart.apply(tr))
								.group(groupNummer)
								.build());
					break;
				}
			}
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
	
	public Boolean getIsBcReport() {
		return this.personInBcReport !=null & this.personInBcReport.isPresent()
			|| this.personInBcReportLastMonat !=null & this.personInBcReportLastMonat.isPresent();
	}
	
	private String getAttribute(PersonInBcReport personInBcReport, String key) {
		return personInBcReport.getAttribute().get(key);
	}

	private String getSaldoLastMonat(PersonInBcReport personInBcReport, String fieldName) {
		return personInBcReport.getSaldo().getSaldoList()
				.stream()
				.filter(s -> fieldName.equals(s.getKSaldo_Bezeichnung()))
				.findFirst()
				.map(u -> u.getGtisBASaldoEndeAktPer()).orElse("0");
	}
	
	public String getLastMonat() {
		return PeriodReport.formatMonth(this.period.getStart().minusMonths(1));
	}
	
	public List<TimeReportRecordFromBcReport> getBcReports() {
		List<TimeReportRecordFromBcReport> reports = new ArrayList<>();
		addToTimeReportRecord(reports, this.personInBcReportLastMonat);
		addToTimeReportRecord(reports, this.personInBcReport);
		return reports;
	}

	private void addToTimeReportRecord(List<TimeReportRecordFromBcReport> reports, Optional<PersonInBcReport> personInBcReport) {
		if (personInBcReport.isEmpty()) {
			return;
		}
		//@formatter:off
		reports.add(TimeReportRecordFromBcReport.builder()
			.month(getAttribute(personInBcReport.get(), "gtxtPeriodenText"))
			.calendarArt(getAttribute(personInBcReport.get(), "gtxtStammAZM"))
			.timeSaldo(getSaldoLastMonat(personInBcReport.get(), "Arbeitsvertrags-Saldo"))
			.timeSaldoSign(getSaldoLastMonat(personInBcReport.get(), "Arbeitsvertrags-Saldo").indexOf("-"))
			.urlaubSaldo(getSaldoLastMonat(personInBcReport.get(), "Urlaubssaldo in Tagen"))
			.sollStunde(countSollStunde(personInBcReport.get()))
			.build());
		//@formatter:on
	}

	private String countSollStunde(PersonInBcReport personInBcReport) {
		Double res = 0.;
		if (personInBcReport.getDateTable()!=null) {
			for (BcReportZeitNachweisDateDescription rec: personInBcReport.getDateTable().getDateTable()) {
				res += TimeUtils.convertTimeToMinuten(rec.getSoll());
			}
		}
		return String.format("%.2f", res/60);
	}

	public Optional<BcReportZeitNachweisDateDescription> getDateTableRecord(LocalDate eventDate) {
		if (this.personInBcReport.isEmpty()) {
			return Optional.empty();
		}
		if (this.personInBcReport.get().getDateTable()==null) {
			return Optional.empty();
		}
		if (this.personInBcReport.get().getDateTable().getDateTable()==null) {
			return Optional.empty();
		}
		for (BcReportZeitNachweisDateDescription rec: this.personInBcReport.get().getDateTable().getDateTable()) {
			if (rec.getDate_Period_Start().startsWith(DateUtils.formattDDMMYYY(eventDate))) {
				return Optional.of(rec);
			}
		}
		return Optional.empty();
	}
	
	private String getSollDateAttribute(LocalDate eventDate) {
		if (this.personInBcReport.isEmpty()) {
			return "";
		}
		Optional<BcReportZeitNachweisDateDescription> dateTableRecordOpt = getDateTableRecord(eventDate);
		if (dateTableRecordOpt.isEmpty()) {
			return "";
		}
		return dateTableRecordOpt.get().getSoll();
	}

}
