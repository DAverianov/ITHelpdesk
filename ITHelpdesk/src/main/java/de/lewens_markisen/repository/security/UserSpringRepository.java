package de.lewens_markisen.repository.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.security.UserSpring;

public interface UserSpringRepository extends JpaRepository<UserSpring, Integer>{
    Optional<UserSpring> findFirstByUsername(String username);
}
