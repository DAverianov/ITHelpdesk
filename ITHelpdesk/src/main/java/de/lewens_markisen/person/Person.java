package de.lewens_markisen.person;

import java.sql.Timestamp;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import de.lewens_markisen.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "person")
public class Person extends BaseEntity {

	@Builder
	public Person(Long id, Long version, Timestamp createdDate, Timestamp lastModifiedDate, String name,
			String bcCode) {
		super(id, version, createdDate, lastModifiedDate);
		this.name = name;
		this.bcCode = bcCode;
		this.nameForSearch = convertToNameForSearch(this.name); 
	}

	@NotNull
	@Size(min = 2, max = 120)
	@Column(name = "name", length = 120)
	private String name;
	
	@NotNull
	@Size(min = 2, max = 120)
	@Column(name = "name_for_search", length = 120)
	private String nameForSearch;

	@Size(min = 1, max = 4)
	@Column(name = "bc_code", length = 4)
	private String bcCode;

	public static String convertToNameForSearch(String name) {
		return StringUtils.lowerCase(StringUtils.deleteWhitespace(name));
	}
	
	@Override
	public String toString() {
		return "Person [bcCode=" + bcCode + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(bcCode, name);
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
		Person other = (Person) obj;
		return Objects.equals(bcCode, other.bcCode) && Objects.equals(name, other.name);
	}
}
