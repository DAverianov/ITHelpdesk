package de.lewens_markisen.client;


import java.util.List;
import java.util.Optional;

import de.lewens_markisen.domain.local_db.instruction.Instruction;

public interface InstructionRestClient {

    List<Instruction> findAllInstructions(String filter);

    Instruction createInstruction(String name, String description);

    Optional<Instruction> findInstruction(Long instructionId);

    void updateInstruction(Long instructionId, String name, String description);

    void deleteInstruction(Long instructionId);
}
