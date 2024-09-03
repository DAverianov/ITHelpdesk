package timeRecordingSystems.domain;

import java.time.LocalDateTime;
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
public class WorkedShift {

	@Id
	private Integer id;
	private Person person;
	private LocalDateTime dateOfShift; 
	private LocalDateTime start;
	private LocalDateTime end;
	
	@Override
	public String toString() {
		return "Shift [person=" + person + ", dateOfShift=" + dateOfShift + ", start=" + start
				+ ", end=" + end + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateOfShift, end, id, person, start);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkedShift other = (WorkedShift) obj;
		return Objects.equals(dateOfShift, other.dateOfShift) && Objects.equals(end, other.end)
				&& Objects.equals(id, other.id) && Objects.equals(person, other.person)
				&& Objects.equals(start, other.start);
	}

}
