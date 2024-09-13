package de.lewens_markisen.timeReport;

import java.util.Objects;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeRecordReport {
	@Builder
	public TimeRecordReport(String name, Integer mo, Integer tu, Integer we, Integer th, Integer fr, Integer sa,
			Integer so) {
		super();
		this.name = name;
		this.mo = mo;
		this.tu = tu;
		this.we = we;
		this.th = th;
		this.fr = fr;
		this.sa = sa;
		this.so = so;
	}

	private String name;
	private Integer mo;
	private Integer tu;
	private Integer we;
	private Integer th;
	private Integer fr;
	private Integer sa;
	private Integer so;
	
	public Integer getSum() {
		return mo + tu + we + th + fr + sa + so;
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
}
