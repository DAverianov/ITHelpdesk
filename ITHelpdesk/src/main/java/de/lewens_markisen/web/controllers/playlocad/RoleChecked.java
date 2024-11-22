package de.lewens_markisen.web.controllers.playlocad;

import de.lewens_markisen.domain.local_db.security.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class RoleChecked {
	private Role role;
	private Boolean active;
}
