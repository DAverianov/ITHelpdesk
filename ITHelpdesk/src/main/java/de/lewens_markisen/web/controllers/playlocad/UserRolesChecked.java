package de.lewens_markisen.web.controllers.playlocad;

import java.util.ArrayList;
import java.util.List;

import de.lewens_markisen.domain.localDb.security.Role;
import de.lewens_markisen.domain.localDb.security.UserSpring;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRolesChecked {

	private UserSpring user;
	private List<Role> allRoles;
	private List<RoleChecked> rolesChecked;

	public UserRolesChecked() {
		super();
	}

	public void fullRoles() {
		List<RoleChecked> rolesCh = new ArrayList<RoleChecked>();
		allRoles.stream().forEach(r -> rolesCh.add(RoleChecked.builder().role(r).build()));
		for (Role role : user.getRoles()) {
			for (RoleChecked roleCh : rolesCh) {
				if (role.equals(roleCh.getRole())) {
					roleCh.setActive(true);
				}
			}
		}
		this.rolesChecked = rolesCh;
	}

}
