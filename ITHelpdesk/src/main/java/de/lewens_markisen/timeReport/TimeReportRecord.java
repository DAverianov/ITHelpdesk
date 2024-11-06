package de.lewens_markisen.timeReport;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import de.lewens_markisen.bc_reports.BcReportZeitNachweisDateDescription;
import de.lewens_markisen.domain.local_db.time_register_event.TimeRegisterEvent;
import de.lewens_markisen.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TimeReportRecord implements Comparable<TimeReportRecord> {
	
	private TimeRegisterEvent timeRegisterEvent;
	private Optional<BcReportZeitNachweisDateDescription> bcReportZeitNachweisDateDescription;
	private String name;
	private LocalDate eventDate;
	@Builder.Default
	private Long mo = 0l;
	@Builder.Default
	private Long tu = 0l;
	@Builder.Default
	private Long we = 0l;
	@Builder.Default
	private Long th = 0l;
	@Builder.Default
	private Long fr = 0l;
	@Builder.Default
	private Long sa = 0l;
	@Builder.Default
	private Long su = 0l;
	@Builder.Default
	private Integer group = 0;
	@Builder.Default
	private Long pause = 0l;

	public Long getSum() {
		return getMo() + getTu() + getWe() + getTh() + getFr() + getSa() + getSu();
	}

	public Long getMo() {
		if (this.timeRegisterEvent==null) {
			return this.mo;
		}
		else {
			return timeOfWorkInDayOfWeek(DayOfWeek.MONDAY);
		}
	}

	public Long getTu() {
		if (this.timeRegisterEvent==null) {
			return this.tu;
		}
		else {
			return timeOfWorkInDayOfWeek(DayOfWeek.TUESDAY);
		}
	}

	public Long getWe() {
		if (this.timeRegisterEvent==null) {
			return this.we;
		}
		else {
			return timeOfWorkInDayOfWeek(DayOfWeek.WEDNESDAY);
		}
	}

	public Long getTh() {
		if (this.timeRegisterEvent==null) {
			return this.th;
		}
		else {
			return timeOfWorkInDayOfWeek(DayOfWeek.THURSDAY);
		}
	}

	public Long getFr() {
		if (this.timeRegisterEvent==null) {
			return this.fr;
		}
		else {
			return timeOfWorkInDayOfWeek(DayOfWeek.FRIDAY);
		}
	}

	public Long getSa() {
		if (this.timeRegisterEvent==null) {
			return this.sa;
		}
		else {
			return timeOfWorkInDayOfWeek(DayOfWeek.SATURDAY);
		}
	}

	public Long getSu() {
		if (this.timeRegisterEvent==null) {
			return this.su;
		}
		else {
			return timeOfWorkInDayOfWeek(DayOfWeek.SUNDAY);
		}
	}

	public String getMoDecimal() {
		return TimeUtils.secondsToHourMinutes(getMo(), true);
	}

	public String getTuDecimal() {
		return TimeUtils.secondsToHourMinutes(getTu(), true);
	}

	public String getWeDecimal() {
		return TimeUtils.secondsToHourMinutes(getWe(), true);
	}

	public String getThDecimal() {
		return TimeUtils.secondsToHourMinutes(getTh(), true);
	}

	public String getFrDecimal() {
		return TimeUtils.secondsToHourMinutes(getFr(), true);
	}

	public String getSaDecimal() {
		return TimeUtils.secondsToHourMinutes(getSa(), true);
	}

	public String getSuDecimal() {
		return TimeUtils.secondsToHourMinutes(getSu(), true);
	}

	public String getSumDecimal() {
		return TimeUtils.secondsToHourMinutes(getSum(), true);
	}

	private Long timeOfWorkInDayOfWeek(DayOfWeek dayOfWeek) {
		if (this.eventDate.getDayOfWeek().equals(dayOfWeek)) {
			return timeOfWorkInDay();
		} else {
			return 0l;
		}
	}

	public Long timeOfWorkInDay() {
		if (this.timeRegisterEvent==null) {
			return 0l;
		}
		//@formatter:off
		if (this.timeRegisterEvent.getStartTime() != null 
				&& !this.timeRegisterEvent.getStartTime().equals("") 
				&& this.timeRegisterEvent.getEndTime() != null 
				&& !this.timeRegisterEvent.getEndTime().equals("")) {
			return timeOfWork(this.timeRegisterEvent.getStartTime(), this.timeRegisterEvent.getEndTime());
		} else {
			return 0l;
		}
		//@formatter:on
	}

	public Long timeOfWork(String startTime, String endTime) {
		long seconds = 0l;
		//@formatter:off
		if (this.eventDate != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H[H]:mm");
			LocalDateTime startDateTime = LocalDateTime.parse(this.eventDate + " " + startTime, formatter);
			LocalDateTime endDateTime = LocalDateTime.parse(this.eventDate + " " + endTime, formatter);
			Duration duration = Duration.between(startDateTime, endDateTime);
			seconds = duration.getSeconds() - pauseLang();
		}
		//@formatter:on
		return seconds;
	}

	public String getYearWeek() {
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
		return "" + getEventDate().getYear() + " " 
				+ String.format("%02d", getEventDate().getMonthValue()) + " " 
				+ getEventDate().get(woy) + " woche";
	}

	public String getYearMonat() {
		//@formatter:off
		return "" + getEventDate().getYear() + " " 
				+ String.format("%02d", getEventDate().getMonthValue()) + " " 
				+ getEventDate().getMonth();
		//@formatter:on
	}

	public String getName() {
		if (this.timeRegisterEvent==null) {
			return this.name;
		}
		return "" + this.timeRegisterEvent.getEventDate() 
				+ "  /" + this.timeRegisterEvent.getStartTime() + " - " + this.timeRegisterEvent.getEndTime() + "/ - "
				+ TimeUtils.secondsToHourMinutes(pauseLang(), false) 
				+ " = " 
				+ TimeUtils.secondsToHourMinutes(timeOfWorkInDay(), false);
	}
	
	public String getSoll() {
		if (this.bcReportZeitNachweisDateDescription==null) {
			return "";
		}
		if (this.bcReportZeitNachweisDateDescription.isEmpty()) {
			return "";
		}
		return this.bcReportZeitNachweisDateDescription.get().getGtisSoll();
	}
	
	private long pauseLang() {
		if (this.bcReportZeitNachweisDateDescription==null) {
			return 0;
		}
		if (this.bcReportZeitNachweisDateDescription.isEmpty()) {
			return 0;
		}
		return (long) this.pause * TimeUtils.SECONDS_PER_MINUTE;
	}

	@Override
	public int compareTo(TimeReportRecord o) {
		if (this.getEventDate().isAfter(o.getEventDate())) {
			return 1;
		} else if (this.getEventDate().isBefore(o.getEventDate())) {
			return -1;
		} else {
			if (this.getGroup() > o.getGroup()) {
				return -1;
			} else if (this.getGroup() < o.getGroup()) {
				return 1;
			} else {
				return this.getName().compareTo(o.getName());
			}
		}
	}

	public void addSumm(TimeReportRecord w) {
		this.mo += w.getMo();
		this.tu += w.getTu();
		this.we += w.getWe();
		this.th += w.getTh();
		this.fr += w.getFr();
		this.sa += w.getSa();
		this.su += w.getSu();
	}

	@Override
	public int hashCode() {
		return Objects.hash(fr, mo, name, sa, su, th, tu, we);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeReportRecord other = (TimeReportRecord) obj;
		return Objects.equals(fr, other.fr) && Objects.equals(mo, other.mo) && Objects.equals(name, other.name)
				&& Objects.equals(sa, other.sa) && Objects.equals(su, other.su) && Objects.equals(th, other.th)
				&& Objects.equals(tu, other.tu) && Objects.equals(we, other.we);
	}

	@Override
	public String toString() {
		return "" + name;
	}
}
