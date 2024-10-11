package de.lewens_markisen.domain.localDb;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import de.lewens_markisen.utils.TimeUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "time_register_event")
public class TimeRegisterEvent extends BaseEntity {

	@Builder
	public TimeRegisterEvent(Long id, Long version, Timestamp createdDate, Timestamp lastModifiedDate, Person person,
			LocalDate eventDate, String startTime, String endTime) {
		super(id, version, createdDate, lastModifiedDate);
		this.person = person;
		this.eventDate = eventDate;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	@ManyToOne
	private Person person;

	@Column(name = "event_date")
	private LocalDate eventDate;

	@Column(name = "start_time")
	private String startTime;

	@Column(name = "end_time")
	private String endTime;

	public Long getMo() {
		return timeOfWorkInDayOfWeek(DayOfWeek.MONDAY);
	}

	public Long getTu() {
		return timeOfWorkInDayOfWeek(DayOfWeek.TUESDAY);
	}

	public Long getWe() {
		return timeOfWorkInDayOfWeek(DayOfWeek.WEDNESDAY);
	}

	public Long getTh() {
		return timeOfWorkInDayOfWeek(DayOfWeek.THURSDAY);
	}

	public Long getFr() {
		return timeOfWorkInDayOfWeek(DayOfWeek.FRIDAY);
	}

	public Long getSa() {
		return timeOfWorkInDayOfWeek(DayOfWeek.SATURDAY);
	}

	public Long getSo() {
		return timeOfWorkInDayOfWeek(DayOfWeek.SUNDAY);
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
		return TimeUtils.secondsToHourMinutes(getSo(), true);
	}

	public String getSoDecimal() {
		return TimeUtils.secondsToHourMinutes(getSo(), true);
	}
	public String getSumDecimal() {
		return TimeUtils.secondsToHourMinutes(getSum(), true);
	}

	private Long timeOfWorkInDayOfWeek(DayOfWeek dayOfWeek) {
		if (this.eventDate.getDayOfWeek().equals(dayOfWeek)) {
			return timeOfWorkInDay(true);
		} else {
			return 0l;
		}
	}

	public Long timeOfWorkInDay(boolean inDecimal) {
		//@formatter:off
		if (this.startTime != null 
				&& !this.startTime.equals("") 
				&& this.endTime != null 
				&& !this.endTime.equals("")) {
//			return secondsToHourMinutes(seconds, inDecimal);
			return timeOfWork();
		} else {
			return 0l;
		}
		//@formatter:on
	}

	public Long timeOfWork() {
		long seconds = 0l;
		//@formatter:off
		if (this.eventDate != null 
				&& this.startTime != null 
				&& !this.startTime.equals("") 
				&& this.endTime != null
				&& !this.endTime.equals("")) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H[H]:mm");
			LocalDateTime startDateTime = LocalDateTime.parse(this.eventDate + " " + this.startTime, formatter);
			LocalDateTime endDateTime = LocalDateTime.parse(this.eventDate + " " + this.endTime, formatter);
			Duration duration = Duration.between(startDateTime, endDateTime);
			seconds = duration.getSeconds() - pauseLang();
		}
		//@formatter:on
		return seconds;
	}

	public Long getSum() {
		return timeOfWork();
	}

	private Long pauseLang() {
		//@formatter:off
		DayOfWeek dw = this.eventDate.getDayOfWeek();
		if (dw.equals(DayOfWeek.FRIDAY)) {
			return 15l * TimeUtils.SECONDS_PER_MINUTE;
		} else if (dw.equals(DayOfWeek.MONDAY) 
				|| dw.equals(DayOfWeek.TUESDAY) 
				|| dw.equals(DayOfWeek.WEDNESDAY)
				|| dw.equals(DayOfWeek.THURSDAY)) {
			return 45l * TimeUtils.SECONDS_PER_MINUTE;
		} else {
			return 0l;
		}
		//@formatter:on
	}

	public String toStringReport() {
		return "" + getEventDate() + "  /" + getStartTime() + " - " + getEndTime() + "/ - "
				+ TimeUtils.secondsToHourMinutes(pauseLang(), false) 
				+ " = " 
				+ TimeUtils.secondsToHourMinutes(timeOfWork(), false);
	}

	@Override
	public String toString() {
		return "TimeRegisterEvent [person=" + person + ", eventDate=" + eventDate + ", startTime=" + startTime
				+ ", endTime=" + endTime + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(endTime, eventDate, person, startTime);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeRegisterEvent other = (TimeRegisterEvent) obj;
		return Objects.equals(endTime, other.endTime) && Objects.equals(eventDate, other.eventDate)
				&& Objects.equals(person, other.person) && Objects.equals(startTime, other.startTime);
	}

}
