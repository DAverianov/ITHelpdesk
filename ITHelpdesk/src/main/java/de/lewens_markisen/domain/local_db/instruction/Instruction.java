package de.lewens_markisen.domain.local_db.instruction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import de.lewens_markisen.domain.local_db.BaseEntity;
import de.lewens_markisen.domain.local_db.security.AuthoritieNames;
import de.lewens_markisen.web.controllers.playlocad.InstructionLinePayload;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "instruction")
public class Instruction extends BaseEntity implements AuthoritieNames{

	@NotNull
	@Size(min = 2, max = 120)
	@Column(name = "name", length = 120)
	private String name;

	@Column(name = "author", length = 60)
	private String author;
	
	@NotNull
	@Size(min = 2, max = 3000)
	@Column(name = "description", length = 3000)
	private String description;
	
    @Singular
	@OneToMany(mappedBy = "instruction", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
	private Set<InstructionLine> instructionLines;

	public void addLine(InstructionLinePayload l) {
		//@formatter:off
		this.instructionLines.add(InstructionLine.builder()
				.instruction(this)
				.stringNummer(l.getStringNummer())
				.description(l.getDescription())
				.build());
		//@formatter:on
	}

	@Override
    public List<String> getAuthoritieNames() {
		List<String> authNames = new ArrayList<String>();
		authNames.add("instruction.create");
		authNames.add("instruction.read");
		authNames.add("instruction.update");
		authNames.add("instruction.delete");
		return authNames;
	}

}
