package de.lewens_markisen.domain.local_db.instruction;

import java.sql.Timestamp;

import de.lewens_markisen.domain.local_db.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "instruction_line")
public class InstructionLine extends BaseEntity {

	public InstructionLine(Instruction instruction,
			@NotNull @Size(min = 1, max = 5) Integer stringNummer,
			@NotNull @Size(min = 2, max = 300) String description) {
		this.stringNummer = stringNummer;
		this.instruction = instruction;
		this.description = description;
	}

	@NotNull
	@Size(min = 1, max = 5)
	@Column(name = "string_nummer", length = 5)
	private Integer stringNummer;
	
    @ManyToOne(fetch = FetchType.EAGER)
	private Instruction instruction;
	
	@NotNull
	@Size(min = 2, max = 300)
	@Column(name = "description", length = 300)
	private String description;

}
