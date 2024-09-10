package de.lewens_markisen.domain;

import java.sql.Timestamp;
import java.time.LocalDate;
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

}
