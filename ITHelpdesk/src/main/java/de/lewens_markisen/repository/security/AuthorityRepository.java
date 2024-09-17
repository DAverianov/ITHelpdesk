package de.lewens_markisen.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.security.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Integer>{

}
