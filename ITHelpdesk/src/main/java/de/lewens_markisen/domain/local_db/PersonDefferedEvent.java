package de.lewens_markisen.domain.local_db;

import de.lewens_markisen.domain.local_db.email.EmailLetter;
import de.lewens_markisen.domain.local_db.security.UserSpring;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "person_deffered_event")
public class PersonDefferedEvent extends BaseEntity {

	@Column(name = "deffered_event")
    @Enumerated(EnumType.STRING)
	private DefferedEvent defferedEvent;
	
	@ManyToOne
	private Person person;

	@ManyToOne
	private UserSpring user;
	
	private Boolean done;
	private String executionResult;
	@ManyToOne
	private EmailLetter email;

}
