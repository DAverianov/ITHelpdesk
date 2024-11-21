package de.lewens_markisen.domain.local_db.person;

import java.sql.Timestamp;
import java.util.Objects;

import de.lewens_markisen.domain.local_db.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "person_presence")
public class PersonPresence extends BaseEntity {
	
	@Builder
	public PersonPresence(Long id, Long version, Timestamp createdDate,
			Timestamp lastModifiedDate, @NotNull Person person,
			Boolean presence, String arrivalTime) {
		super(id, version, createdDate, lastModifiedDate);
		this.person = person;
		this.presence = presence;
		this.arrivalTime = arrivalTime;
	}

	@NotNull
	@ManyToOne
	private Person person;
	
	private Boolean presence;
	
	private String arrivalTime;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(arrivalTime, person, presence);
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
		PersonPresence other = (PersonPresence) obj;
		return Objects.equals(arrivalTime, other.arrivalTime)
				&& Objects.equals(person, other.person)
				&& Objects.equals(presence, other.presence);
	}

}
