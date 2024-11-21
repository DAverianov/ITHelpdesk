package de.lewens_markisen.domain.local_db.person;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import de.lewens_markisen.domain.local_db.BaseEntity;
import de.lewens_markisen.domain.local_db.security.AuthoritieNames;
import de.lewens_markisen.utils.DateUtils;
import de.lewens_markisen.utils.StringUtilsLSS;
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
public class Person extends BaseEntity implements AuthoritieNames {

	@Builder
	public Person(Long id, Long version, Timestamp createdDate,
			Timestamp lastModifiedDate, String name, String nameForSearch,
			String bcCode, LocalDate dateOfBirthday, LocalDate hiringDate,
			LocalDate firingDate) {
		super(id, version, createdDate, lastModifiedDate);
		this.name = name;
		this.bcCode = bcCode;
		this.hiringDate = hiringDate;
		this.firingDate = firingDate;
		this.dateOfBirthday = dateOfBirthday;
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

	private LocalDate hiringDate;
	private LocalDate firingDate;
	private LocalDate dateOfBirthday;

	public static String convertToNameForSearch(String name) {
		return StringUtilsLSS.replaceUmlauts(
				StringUtils.lowerCase(StringUtils.deleteWhitespace(name)));
	}

	public String getFiringDateStr() {
		if (firingDate == null || firingDate.equals(LocalDate.of(1, 1, 1))) {
			return "";
		}
		else {
			return DateUtils.formattDDMMYYY(firingDate);
		}
	}

	public String getHiringDateStr() {
		return DateUtils.formattDDMMYYY(hiringDate);
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
		return Objects.equals(bcCode, other.bcCode)
				&& Objects.equals(name, other.name);
	}

	@Override
	public List<String> getAuthoritieNames() {
		List<String> authNames = new ArrayList<String>();
		authNames.add("person.create");
		authNames.add("person.read");
		authNames.add("person.update");
		authNames.add("person.delete");
		return authNames;
	}
}
