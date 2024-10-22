package de.lewens_markisen.web.controllers.playlocad;

import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

@Getter
@Setter
@Builder
public class NewInstructionPayload {
	
    @NotNull(message = "{intruction.create.errors.title_is_null}")
    @Size(min = 2, max = 120, message = "{instruction.create.errors.title_size_is_invalid}")
    private String name;
    
    @Size(min = 2, max = 3000, message = "{instruction.create.errors.details_size_is_invalid}")
    private String description;
    
    @Singular
    private Set<InstructionLinePayload> lines;
    
    public List<String> getLinesColumnNames(){
    	return InstructionLinePayload.getColumnNames();
    }
}
