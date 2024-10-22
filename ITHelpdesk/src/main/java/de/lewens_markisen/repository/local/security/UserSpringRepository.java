package de.lewens_markisen.repository.local.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.local_db.security.UserSpring;

public interface UserSpringRepository extends JpaRepository<UserSpring, Integer>{
    Optional<UserSpring> findFirstByUsername(String username);
    Optional<UserSpring> findByUsername(String username);
}
