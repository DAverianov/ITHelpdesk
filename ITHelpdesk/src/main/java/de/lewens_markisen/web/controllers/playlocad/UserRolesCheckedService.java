package de.lewens_markisen.web.controllers.playlocad;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.local_db.security.Role;
import de.lewens_markisen.domain.local_db.security.UserSpring;
import de.lewens_markisen.security.RoleService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserRolesCheckedService {
	
	private final RoleService roleService;
	
	public UserRolesChecked createUserRolesChecked(UserSpring user) {
		return UserRolesChecked.builder()
				.user(user)
				.rolesChecked(getRolesChecked(user))
				.build();
	}

	private List<RoleChecked> getRolesChecked(UserSpring user) {
		List<RoleChecked> rolesCh = new ArrayList<RoleChecked>();
		Set<Role> userRoles = user.getRoles();
		//@formatter:off
		roleService.findAll()
			.stream()
			.forEach(r -> rolesCh.add(
				RoleChecked.builder()
					.role(r.getName())
					.id(r.getId())
					.active(userRoles.contains(r) ? true : false)
					.build()));
		//@formatter:on
		return rolesCh;
	}
}
