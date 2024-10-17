package de.lewens_markisen.repository.local;

import org.springframework.data.jpa.repository.JpaRepository;
import de.lewens_markisen.domain.localDb.instruction.InstructionLine;

public interface InstructionLineRepository extends JpaRepository<InstructionLine, Long> {

}
