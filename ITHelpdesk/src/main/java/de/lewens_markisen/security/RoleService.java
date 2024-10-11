package de.lewens_markisen.security;

import java.util.List;
import java.util.Optional;

import de.lewens_markisen.domain.localDb.security.Role;

public interface RoleService {
	public Role saveIfNotExist(Role role);
	public Optional<Role> findByName(String roleName);
	public void saveAll(List<Role> roles);
}
