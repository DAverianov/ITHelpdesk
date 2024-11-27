package de.lewens_markisen.repository.local.security;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.local_db.security.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
	List<Authority> findByPermission(String permission);
    Optional<Authority> findFirstByPermission(String permission);
}
