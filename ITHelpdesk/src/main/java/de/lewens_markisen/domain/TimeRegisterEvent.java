package de.lewens_markisen.domain;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;

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
			LocalDate eventDate, String startDate, String endDate) {
		super(id, version, createdDate, lastModifiedDate);
		this.person = person;
		this.eventDate = eventDate;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@ManyToOne
	private Person person;

	@Column(name = "event_date")
	private LocalDate eventDate;

	@Column(name = "start_date")
	private String startDate;

	@Column(name = "end_date")
	private String endDate;
    
	public String getMo() {
        return this.endDate;
    }
    
	public String getDi() {
        return this.endDate;
    }

	@Override
	public String toString() {
		return "TimeRegisterEvent [person=" + person + ", eventDate=" + eventDate + ", startDate=" + startDate
				+ ", endDate=" + endDate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(endDate, eventDate, person, startDate);
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
		return Objects.equals(endDate, other.endDate) && Objects.equals(eventDate, other.eventDate)
				&& Objects.equals(person, other.person) && Objects.equals(startDate, other.startDate);
	}

}
