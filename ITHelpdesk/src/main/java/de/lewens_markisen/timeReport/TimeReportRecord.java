package de.lewens_markisen.timeReport;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import de.lewens_markisen.timeRegisterEvent.TimeRegisterEvent;
import de.lewens_markisen.utils.TimeUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeReportRecord {

	@Builder
	public TimeReportRecord(String name) {
		super();
		this.name = name;
	}

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
	}

	private String name;
	private LocalDate eventDate;
	private Long mo = 0l;
	private Long tu = 0l;
	private Long we = 0l;
	private Long th = 0l;
	private Long fr = 0l;
	private Long sa = 0l;
	private Long so = 0l;
	
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
		setMo(this.getMo()+tr.getMo());
		setTu(this.getTu()+tr.getTu());
		setWe(this.getWe()+tr.getWe());
		setTh(this.getTh()+tr.getTh());
		setFr(this.getFr()+tr.getFr());
		setSo(this.getSo()+tr.getSo());
		setSa(this.getSa()+tr.getSa());
	}

	public String getYearWeek() {
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
		return "" + getEventDate().getYear() + " Woche " + getEventDate().get(woy);
	}

	public String getYearMonat() {
		return "" + getEventDate().getYear() + " " + getEventDate().getMonthValue();
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
}
