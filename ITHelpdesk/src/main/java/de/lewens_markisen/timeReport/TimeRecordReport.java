package de.lewens_markisen.timeReport;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import de.lewens_markisen.timeRegisterEvent.TimeRegisterEvent;
import de.lewens_markisen.utils.TimeUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeRecordReport {

	@Builder
	public TimeRecordReport(String name) {
		super();
		this.name = name;
	}

	private String name;
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

	public String secondsToHourMinutes(long seconds, boolean inDecimal) {
		if (seconds > 0) {
			if (inDecimal) {
				return "" + String.format("%.2f", (double) seconds / TimeUtils.SECONDS_PER_HOUR);
			} else {
				int hours = (int) (seconds / TimeUtils.SECONDS_PER_HOUR);
				int minutes = (int) seconds % TimeUtils.SECONDS_PER_HOUR / 60;
				return "" + hours + ":" + String.format("%02d", minutes);
			}
		} else {
			return "--";
		}
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
		TimeRecordReport other = (TimeRecordReport) obj;
		return Objects.equals(fr, other.fr) && Objects.equals(mo, other.mo) && Objects.equals(name, other.name)
				&& Objects.equals(sa, other.sa) && Objects.equals(so, other.so) && Objects.equals(th, other.th)
				&& Objects.equals(tu, other.tu) && Objects.equals(we, other.we);
	}

	@Override
	public String toString() {
		return "" + name;
	}
}
