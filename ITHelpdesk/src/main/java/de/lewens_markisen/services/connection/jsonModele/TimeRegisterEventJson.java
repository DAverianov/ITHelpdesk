package de.lewens_markisen.services.connection.jsonModele;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TimeRegisterEventJson {
	@JsonProperty("Person")
	private String person;
	@JsonProperty("name")
	private String name;

	@JsonProperty("Von_Datum")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate eventDate;

	@JsonProperty("Startzeit")
	private String startDate;

	@JsonProperty("Endezeit")
	private String endDate;

}
