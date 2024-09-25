package de.lewens_markisen.services.connection.jsonModele;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersonBcJsonList {
	private String dataContent;
	 @JsonProperty("value")
	private List<PersonBcJson> value;

}
