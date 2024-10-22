package de.lewens_markisen.domain.local_db;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.lewens_markisen.domain.local_db.security.AuthoritieNames;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
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
@Table(name = "accesses")
public class Access extends BaseEntity implements AuthoritieNames{

	@Builder
	//@formatter:off
	public Access(Long id, Long version, Timestamp createdDate, Timestamp lastModifiedDate,
			@NotNull @Size(min = 2, max = 120) String name, 
			@Size(min = 2, max = 300) String url, 
			@Size(min = 2, max = 60) String domain,
			@Size(min = 2, max = 120) String user, 
			@Size(min = 2, max = 50) String password,
			@Size(min = 2, max = 200) String description) {
	//@formatter:on
		super(id, version, createdDate, lastModifiedDate);
		this.name = name;
		this.url = url;
		this.domain = domain;
		this.user = user;
		this.password = password;
		this.description = description;
	}

	@NotNull
	@Size(min = 2, max = 120)
	@Column(name = "name", length = 120)
	private String name;

	@Size(min = 2, max = 300)
	@Column(name = "url", length = 300)
	private String url;

	@Size(min = 2, max = 60)
	@Column(name = "domain", length = 60)
	private String domain;

	@Size(min = 2, max = 120)
	@Column(name = "user_", length = 120)
	private String user;

	@Size(min = 2, max = 50)
	@Column(name = "password", length = 200)
	@Convert(converter = PasswordConverter.class)
	private String password;

	@Size(min = 2, max = 200)
	@Column(name = "description", length = 200)
	private String description;

	@PrePersist
	public void prePersistCallback() {
		System.out.println("JPA PrePersist Callback was called");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(domain, name, user);
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
		Access other = (Access) obj;
		return Objects.equals(domain, other.domain) && Objects.equals(name, other.name)
				&& Objects.equals(user, other.user);
	}

	@Override
	public String toString() {
		return "Access [name=" + name + ", domain=" + domain + ", user=" + user + ", password=*******" + "]";
	}

	@Override
	public List<String> getAuthoritieNames() {
		List<String> authNames = new ArrayList<String>();
		authNames.add("access.create");
		authNames.add("access.read");
		authNames.add("access.update");
		authNames.add("access.delete");
		return authNames;
	}

}
