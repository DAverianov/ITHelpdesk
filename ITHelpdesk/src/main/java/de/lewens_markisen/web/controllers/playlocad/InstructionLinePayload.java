package de.lewens_markisen.web.controllers.playlocad;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InstructionLinePayload {
	private Integer stringNummer;
	private String description;
}
