package de.lewens_markisen.domain;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "accesses")
public class Access extends BaseEntity {
	@NotNull
	@Size(min = 2, max = 120)
	@Column(name = "name", length = 120)
	private String name;
	@NotNull
	@Size(min = 2, max = 50)
	@Column(name = "password", length = 200)
    @Convert(converter = PasswordConverter.class)
	private String password;

    @PrePersist
    public void prePersistCallback(){
        System.out.println("JPA PrePresist Callback was called");
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(name, password);
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
		return Objects.equals(name, other.name) && Objects.equals(password, other.password);
	}

}
