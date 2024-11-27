package de.lewens_markisen.timeReport;

import java.util.HashSet;
import java.util.Set;

import de.lewens_markisen.utils.TimeUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TimeReportGroupRecords {
	private String name;
	@Builder.Default
	private Set<TimeReportRecord> elements = new HashSet<TimeReportRecord>();
	
	public void addElement(TimeReportRecord rec) {
		this.elements.add(rec);
	}
	
	public Long getMo() {
		return elements.stream().mapToLong(TimeReportRecord::getMo).sum();
	}
	
	public Long getTu() {
		return elements.stream().mapToLong(TimeReportRecord::getTu).sum();
	}
	
	public Long getWe() {
		return elements.stream().mapToLong(TimeReportRecord::getWe).sum();
	}
	
	public Long getTh() {
		return elements.stream().mapToLong(TimeReportRecord::getTh).sum();
	}
	
	public Long getFr() {
		return elements.stream().mapToLong(TimeReportRecord::getFr).sum();
	}
	
	public Long getSa() {
		return elements.stream().mapToLong(TimeReportRecord::getSa).sum();
	}
	
	public Long getSu() {
		return elements.stream().mapToLong(TimeReportRecord::getSu).sum();
	}
	
	public Long getSum() {
		return getMo() + getTu() + getWe() + getTh() + getFr() + getSa() + getSu();
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
}
