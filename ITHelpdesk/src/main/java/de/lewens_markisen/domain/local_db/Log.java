package de.lewens_markisen.domain.local_db;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.lewens_markisen.domain.local_db.security.AuthoritieNames;
import de.lewens_markisen.domain.local_db.security.UserSpring;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "log")
public class Log extends BaseEntity implements AuthoritieNames{

	@NotNull
	@Size(min = 2, max = 50)
	@Column(name = "event", length = 50)
	private String event;

	@ManyToOne
	private UserSpring user;
	
	@NotNull
	@Size(min = 2, max = 300)
	@Column(name = "description", length = 300)
	private String description;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(event, user);
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
		Log other = (Log) obj;
		return Objects.equals(event, other.event) && Objects.equals(user, other.user);
	}

	@Override
	public List<String> getAuthoritieNames() {
		List<String> authNames = new ArrayList<String>();
		authNames.add("log.read");
		return authNames;
	}

}
