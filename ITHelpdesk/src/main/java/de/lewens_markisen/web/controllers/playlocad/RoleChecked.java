package de.lewens_markisen.web.controllers.playlocad;

import de.lewens_markisen.domain.localDb.security.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoleChecked {
	private Role role;
	private Boolean active;
}
