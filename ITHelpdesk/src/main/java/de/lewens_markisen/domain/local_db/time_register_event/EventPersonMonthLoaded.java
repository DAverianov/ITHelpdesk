package de.lewens_markisen.domain.local_db.time_register_event;

import java.time.LocalDate;

import de.lewens_markisen.domain.local_db.BaseEntity;
import de.lewens_markisen.domain.local_db.Person;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "event_person_month_loaded")
public class EventPersonMonthLoaded extends BaseEntity{

	public EventPersonMonthLoaded(Person person, EventPersonMonth event, LocalDate month) {
		this.person = person;
		this.month = month;
		this.event = event;
	}

	@ManyToOne
	private Person person;
	
	@Column(name = "event")
    @Enumerated(EnumType.STRING)
	private EventPersonMonth event;

	@Column(name = "month", columnDefinition = "DATE")
	private LocalDate month;

}
