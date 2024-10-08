package de.lewens_markisen.repository.local.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.localDb.security.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
	Optional<Authority> findByPermission(String permission);
}
