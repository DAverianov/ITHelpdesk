package de.lewens_markisen.security;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.local_db.security.Role;
import de.lewens_markisen.repository.local.security.RoleRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RoleServiceImpl implements RoleService{
	private final RoleRepository roleRepository;

	@Override
	public Role saveIfNotExist(Role role) {
		Optional<Role> roleOpt = roleRepository.findByName(role.getName());
		if (roleOpt.isPresent()) {
			return roleOpt.get();
		}
		else {
			return roleRepository.save(role);
		}
	}

	@Override
	public void saveAll(List<Role> roles) {
		roleRepository.saveAll(roles);
	}

	@Override
	public Optional<Role> findByName(String roleName) {
		return roleRepository.findByName(roleName);
	}

	@Override
	public List<Role> findAll() {
		return roleRepository.findAll();
	}

}
