package de.lewens_markisen.web.controllers.playlocad;

import de.lewens_markisen.domain.local_db.person.Person;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class PersonWithGlock {
	private Person person;
	private Boolean glock;
	private Boolean inPresence;
	private String arrivalTime;
}
