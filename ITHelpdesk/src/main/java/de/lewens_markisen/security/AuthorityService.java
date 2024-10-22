package de.lewens_markisen.security;

import java.util.Optional;

import de.lewens_markisen.domain.local_db.security.Authority;

public interface AuthorityService {
	public Optional<Authority> findByPermission(String permission);
	public Authority save(Authority auth);
	public Authority saveIfNotExist(Authority auth);
	public long count();

}
