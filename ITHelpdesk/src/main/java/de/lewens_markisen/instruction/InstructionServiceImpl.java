package de.lewens_markisen.instruction;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.localDb.instruction.Instruction;
import de.lewens_markisen.repository.local.InstructionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InstructionServiceImpl implements InstructionService{
	private final InstructionRepository instructionRepository;

	@Override
	public Page<Instruction> findAll(Pageable pageable) {
		return instructionRepository.findAll(pageable);
	}

	@Override
	public Instruction save(Instruction instruction) {
		return instructionRepository.save(instruction);
	}

	@Override
	public Optional<Instruction> findById(Long id) {
		return instructionRepository.findById(id);
	}

}
