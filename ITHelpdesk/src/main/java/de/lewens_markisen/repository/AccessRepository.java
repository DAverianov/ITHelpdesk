package de.lewens_markisen.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.access.Access;

public interface AccessRepository extends JpaRepository<Access, Long>{
	public Page<Access> findAllByName(String name, Pageable pageable);
	public List<Access> findByName(String name);

}
