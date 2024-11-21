package de.lewens_markisen.domain.local_db.person;

import java.sql.Timestamp;

import de.lewens_markisen.domain.local_db.BaseEntity;
import de.lewens_markisen.domain.local_db.email.EmailLetter;
import de.lewens_markisen.domain.local_db.security.UserSpring;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "person_deffered_event")
public class PersonDefferedEvent extends BaseEntity {

	@Builder
	public PersonDefferedEvent(Long id, Long version, Timestamp createdDate,
			Timestamp lastModifiedDate, DefferedEvent defferedEvent,
			Person person, UserSpring user, Boolean done,
			String executionResult) {
		super(id, version, createdDate, lastModifiedDate);
		this.defferedEvent = defferedEvent;
		this.person = person;
		this.user = user;
		if (done==null) {
			this.done = false;
		}
		else {
			this.done = done;
		}
		this.executionResult = executionResult;
	}
	@Column(name = "deffered_event")
    @Enumerated(EnumType.STRING)
	@NotNull
	private DefferedEvent defferedEvent;
	
	@ManyToOne
	private Person person;

	@ManyToOne
	private UserSpring user;
	
    private Boolean done = false;
    
	private String executionResult;

}
