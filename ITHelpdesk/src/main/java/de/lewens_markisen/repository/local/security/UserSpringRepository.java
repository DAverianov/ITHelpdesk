package de.lewens_markisen.repository.local.security;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.localDb.security.UserSpring;

public interface UserSpringRepository extends JpaRepository<UserSpring, Integer>{
    Optional<UserSpring> findFirstByUsername(String username);
    List<UserSpring> findByUsername(String username);
}
