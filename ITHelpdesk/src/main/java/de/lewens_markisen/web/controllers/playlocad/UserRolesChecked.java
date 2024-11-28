package de.lewens_markisen.web.controllers.playlocad;

import java.util.List;

import de.lewens_markisen.domain.local_db.security.UserSpring;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRolesChecked {

	private UserSpring user;
	private List<RoleChecked> rolesChecked;

	@Override
	public String toString() {
		return "UserRolesChecked [user=" + user
				+ ", rolesChecked=" + rolesChecked + "]";
	}

}
