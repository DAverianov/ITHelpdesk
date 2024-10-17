package de.lewens_markisen.instruction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.lewens_markisen.domain.localDb.instruction.Instruction;

public interface InstructionService {

	Page<Instruction> findAll(Pageable pageable);

}
