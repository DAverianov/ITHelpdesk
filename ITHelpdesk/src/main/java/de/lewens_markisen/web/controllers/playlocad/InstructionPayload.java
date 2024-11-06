package de.lewens_markisen.web.controllers.playlocad;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class InstructionPayload {
	
	private Long id;
	
	@NotNull(message = "{intruction.create.errors.title_is_null}")
    @Size(min = 2, max = 120, message = "{instruction.create.errors.title_size_is_invalid}")
    private String name;
    
    @Size(min = 2, max = 3000, message = "{instruction.create.errors.details_size_is_invalid}")
    private String description;
    
    @Singular
    private List<InstructionLinePayload> lines;
    
    public List<String> getLinesColumnNames(){
    	return InstructionLinePayload.getColumnNames();
    }
}
