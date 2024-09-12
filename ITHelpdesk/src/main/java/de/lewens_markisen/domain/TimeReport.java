package de.lewens_markisen.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Component
public class TimeReport {
	@Builder
	public TimeReport(Person person, List<TimeRegisterEvent> timeRecords, String comment) {
		super();
		this.person = person;
		this.timeRecords = timeRecords;
		this.comment = comment;
	}
	private Person person;
	private List<TimeRegisterEvent> timeRecords;
	private String comment;
}
