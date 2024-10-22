package de.lewens_markisen.repository.local.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.local_db.security.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	Optional<Role> findByName(String userRole);

}
