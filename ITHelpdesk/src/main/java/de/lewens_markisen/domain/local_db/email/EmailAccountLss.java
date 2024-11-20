package de.lewens_markisen.domain.local_db.email;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import de.lewens_markisen.domain.local_db.Access;
import de.lewens_markisen.domain.local_db.BaseEntity;
import de.lewens_markisen.domain.local_db.security.AuthoritieNames;
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
@Entity
@Table(name = "email_account")
public class EmailAccountLss extends BaseEntity implements AuthoritieNames{
	
	@Builder
	public EmailAccountLss(Long id, Long version, Timestamp createdDate,
			Timestamp lastModifiedDate,
			@NotNull @Size(min = 2, max = 120) String name,
			@NotNull @Size(min = 12, max = 120) String email, String host,
			Integer port, String username, String outgoingProtocol,
			String smtpAuth, String smtpStarttlsEnable,
			@Size(min = 0, max = 300) String description, Access access) {
		super(id, version, createdDate, lastModifiedDate);
		this.name = name;
		this.email = email;
		this.host = host;
		this.port = port;
		this.username = username;
		this.outgoingProtocol = outgoingProtocol;
		this.smtpAuth = smtpAuth;
		this.smtpStarttlsEnable = smtpStarttlsEnable;
		this.description = description;
		this.access = access;
	}

	@NotNull
	@Size(min = 2, max = 120)
	private String name;
	@NotNull
	@Size(min = 12, max = 120)
	private String email;
	
	private String host;
	private Integer port;
	private String username;
	private String outgoingProtocol;
	private String smtpAuth;
	private String smtpStarttlsEnable;
	@Size(min = 0, max = 300)
	private String description;
	@ManyToOne
	private Access access;
	
	@Override
	public List<String> getAuthoritieNames() {
		List<String> authNames = new ArrayList<String>();
		authNames.add("email_account.create");
		authNames.add("email_account.read");
		authNames.add("email_account.update");
		authNames.add("email_account.delete");
		return authNames;
	}
}
