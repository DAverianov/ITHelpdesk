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
public class PersonBcJson {
	@JsonProperty("Code")
	private String Code;
	
	@JsonProperty("Name")
	private String name;

	@JsonProperty("Benutzer")
	private String benutzer;
	
	@JsonProperty("Geburtsdatum")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate geburtsdatum;
	
	@JsonProperty("Konzernaustritt_SOC")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate konzernaustritt_SOC;

}
