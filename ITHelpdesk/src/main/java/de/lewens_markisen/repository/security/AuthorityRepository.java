package de.lewens_markisen.repository.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.security.Authority;
import de.lewens_markisen.domain.security.User;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
	Optional<Authority> findByRole(String role);
}
