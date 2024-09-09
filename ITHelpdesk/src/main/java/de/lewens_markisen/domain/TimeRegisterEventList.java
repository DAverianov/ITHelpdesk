package de.lewens_markisen.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeRegisterEventList {
	private String dataContent;
	 @JsonProperty("value")
	 private List<TimeRegisterEvent> value;
}
