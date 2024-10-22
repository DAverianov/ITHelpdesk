package de.lewens_markisen.web.controllers.playlocad;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InstructionLinePayload {
	private Integer id;
	private Integer stringNummer;
	private String description;
	   
    public static List<String> getColumnNames(){
    	return List.of("stringNummer", "description");
    }
}
