package de.lewens_markisen.repository.local;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.local_db.Access;

public interface AccessRepository extends JpaRepository<Access, Long>{
	public Page<Access> findAllByName(String name, Pageable pageable);
	public List<Access> findByName(String name);
	public Optional<Access> findById(Long id);
}
