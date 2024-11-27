package de.lewens_markisen.instruction;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.local_db.instruction.InstructionLine;
import de.lewens_markisen.repository.local.InstructionLineRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InstructionLineServiceImpl implements InstructionLineService{
	
	private final InstructionLineRepository instructionLineRepository;
	
	@Override
	public InstructionLine save(InstructionLine line) {
		return instructionLineRepository.save(line);
	}

}
