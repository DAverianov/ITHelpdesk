package de.lewens_markisen.repository.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.security.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	Optional<Role> findByName(String userRole);

}
