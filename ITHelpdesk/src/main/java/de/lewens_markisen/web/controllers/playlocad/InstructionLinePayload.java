package de.lewens_markisen.web.controllers.playlocad;

import java.util.List;

import de.lewens_markisen.domain.local_db.instruction.InstructionLine;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InstructionLinePayload {
	
	public InstructionLinePayload(Long id, Integer stringNummer, String description) {
		super();
		this.id = id;
		this.stringNummer = stringNummer;
		this.description = description;
	}

	public InstructionLinePayload(InstructionLine line) {
		super();
		this.id = line.getId();
		this.stringNummer = line.getStringNummer();
		this.description = line.getDescription();
	}

	private Long id;
	private Integer stringNummer;
	private String description;
	   
    public static List<String> getColumnNames(){
    	return List.of("stringNummer", "description");
    }
}
