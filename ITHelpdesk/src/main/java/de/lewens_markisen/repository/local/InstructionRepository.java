package de.lewens_markisen.repository.local;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import de.lewens_markisen.domain.local_db.instruction.Instruction;

public interface InstructionRepository extends JpaRepository<Instruction, Long> {

	@Transactional(readOnly = true)
	@Cacheable("instruction")
	public Page<Instruction> findAll(Pageable pageable) throws DataAccessException;

}
