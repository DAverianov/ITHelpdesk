package de.lewens_markisen.instruction;

import java.util.ArrayList;
import java.util.List;

import de.lewens_markisen.domain.local_db.instruction.Instruction;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InstructionList {

	private List<Instruction> instructions;

	@XmlElement
	public List<Instruction> getInstructionList() {
		if (instructions == null) {
			instructions = new ArrayList<>();
		}
		return instructions;
	}

}
