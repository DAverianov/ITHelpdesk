package de.lewens_markisen.timeRecordingSystems.domain;

import java.time.LocalDateTime;

import de.lewens_markisen.domain.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkedShift {

	private Person person;
	private LocalDateTime dateOfShift; 
	private LocalDateTime start;
	private LocalDateTime end;
	
	@Override
	public String toString() {
		return "Shift [person=" + person + ", dateOfShift=" + dateOfShift + ", start=" + start
				+ ", end=" + end + "]";
	}

}
