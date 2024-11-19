package de.lewens_markisen.domain.local_db.email;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import de.lewens_markisen.domain.local_db.BaseEntity;
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
@Table(name = "email")
public class EmailLetter extends BaseEntity implements AuthoritieNames{
	
	@ManyToOne
	private EmailAcсount sender;
	
	@NotNull
	@Size(min = 10, max = 120)
	@Column(name = "recipient", length = 120)
	private String recipient;
	
	@NotNull
	@Size(min = 2, max = 120)
	@Column(name = "subject", length = 120)
	private String subject;
	
	private String text;
	@ManyToOne
	private UserSpring author;
	private LocalDateTime dateToSenden;
	private LocalDateTime boundaryDate;
	private LocalDateTime sentDate;
	private Boolean sent;

	@Override
    public List<String> getAuthoritieNames() {
		List<String> authNames = new ArrayList<String>();
		authNames.add("email_letter.create");
		authNames.add("email_letter.read");
		authNames.add("email_letter.update");
		authNames.add("email_letter.delete");
		return authNames;
	}
}
