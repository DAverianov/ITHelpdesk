package de.lewens_markisen.timeReport;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Objects;

import de.lewens_markisen.timeRegisterEvent.TimeRegisterEvent;
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

	public TimeReportRecord(TimeRegisterEvent tr) {
		this.name = tr.toStringReport();
		this.eventDate = tr.getEventDate();
		this.mo = tr.getMo();
		this.tu = tr.getTu();
		this.we = tr.getWe();
		this.th = tr.getTh();
		this.fr = tr.getFr();
		this.sa = tr.getSa();
		this.so = tr.getSo();
		this.group = 0;
	}

	public TimeReportRecord(TimeReportRecord tr) {
		this.name = tr.getName();
		this.eventDate = tr.getEventDate();
		this.mo = tr.getMo();
		this.tu = tr.getTu();
		this.we = tr.getWe();
		this.th = tr.getTh();
		this.fr = tr.getFr();
		this.sa = tr.getSa();
		this.so = tr.getSo();
		this.group = tr.getGroup();
	}

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
	private Long so = 0l;
	@Builder.Default
	private Integer group = 0;

	public Long getSum() {
		return mo + tu + we + th + fr + sa + so;
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

	public String getSoDecimal() {
		return TimeUtils.secondsToHourMinutes(getSo(), true);
	}

	public String getSumDecimal() {
		return TimeUtils.secondsToHourMinutes(getSum(), true);
	}

	public void addRecord(TimeRegisterEvent tr) {
		setMo(this.getMo() + tr.getMo());
		setTu(this.getTu() + tr.getTu());
		setWe(this.getWe() + tr.getWe());
		setTh(this.getTh() + tr.getTh());
		setFr(this.getFr() + tr.getFr());
		setSo(this.getSo() + tr.getSo());
		setSa(this.getSa() + tr.getSa());
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

	@Override
	public int hashCode() {
		return Objects.hash(fr, mo, name, sa, so, th, tu, we);
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
				&& Objects.equals(sa, other.sa) && Objects.equals(so, other.so) && Objects.equals(th, other.th)
				&& Objects.equals(tu, other.tu) && Objects.equals(we, other.we);
	}

	@Override
	public String toString() {
		return "" + name;
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
		this.so += w.getSo();
	}
}
