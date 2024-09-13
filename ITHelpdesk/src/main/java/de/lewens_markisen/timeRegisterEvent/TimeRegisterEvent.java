package de.lewens_markisen.timeRegisterEvent;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import de.lewens_markisen.domain.BaseEntity;
import de.lewens_markisen.person.Person;
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
    static final int MINUTES_PER_HOUR = 60;
    static final int SECONDS_PER_MINUTE = 60;
    static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
	
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
    
	public String getMo() {
		return presentationOfDaysTime(DayOfWeek.MONDAY);
    }
 	public String getDi() {
		return presentationOfDaysTime(DayOfWeek.TUESDAY);
    }
	public String getMi() {
		return presentationOfDaysTime(DayOfWeek.WEDNESDAY);
	}
	public String getDo() {
		return presentationOfDaysTime(DayOfWeek.THURSDAY);
	}
	public String getFr() {
		return presentationOfDaysTime(DayOfWeek.FRIDAY);
	}
	public String getSa() {
		return presentationOfDaysTime(DayOfWeek.SATURDAY);
	}
	public String getSo() {
		return presentationOfDaysTime(DayOfWeek.SUNDAY);
	}

	private String presentationOfDaysTime(DayOfWeek dayOfWeek) {
		if(this.eventDate.getDayOfWeek()==dayOfWeek) {
			if (this.startTime!=null & this.startTime!="" & this.endTime!=null & this.endTime!="") {
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H[H]:mm");
		        LocalDateTime startDateTime = LocalDateTime.parse(this.eventDate+" "+this.startTime, formatter);
		        LocalDateTime endDateTime = LocalDateTime.parse(this.eventDate+" "+this.endTime, formatter);
		        Duration duration = Duration.between(startDateTime, endDateTime);
		        long seconds = duration.getSeconds();

				return secondsToHourMinutes(seconds);
			}
			else {
				return "";
			}
		}
		else {
			return "";
		}
	}

	private String secondsToHourMinutes(long seconds) {
		if (seconds>0) {
	        int hours = (int) (seconds / SECONDS_PER_HOUR);
	        int minutes = (int) seconds % SECONDS_PER_HOUR / 60;
			return ""+hours+":" + String.format("%02d", minutes); 
		}
		else {
			return "--";
		}
	}
	public String toStringReport() {
		return "" + getEventDate() + " " + getStartTime() + " - " + getEndTime();
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
