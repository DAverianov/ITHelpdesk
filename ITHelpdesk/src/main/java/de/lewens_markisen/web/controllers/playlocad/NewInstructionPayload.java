package de.lewens_markisen.web.controllers.playlocad;

import java.util.Set;

import de.lewens_markisen.domain.localDb.instruction.InstructionLine;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NewInstructionPayload {
	
    @NotNull(message = "{intruction.create.errors.title_is_null}")
    @Size(min = 2, max = 120, message = "{instruction.create.errors.title_size_is_invalid}")
    private String name;
    
    @Size(max = 3000, message = "{instruction.create.errors.details_size_is_invalid}")
    private String description;
    
    private Set<InstructionLine> lines;
}
