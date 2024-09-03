package timeRecordingSystems.domain;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Person {
	@Id
	private Integer id;
	private String bcCode;
	private String name;

	@Override
	public String toString() {
		return "Person [bcCode=" + bcCode + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(bcCode, id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		return Objects.equals(bcCode, other.bcCode) && Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}
}
