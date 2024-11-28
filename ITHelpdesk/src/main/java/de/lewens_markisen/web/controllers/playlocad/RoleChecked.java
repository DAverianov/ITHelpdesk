package de.lewens_markisen.web.controllers.playlocad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class RoleChecked {
	private Integer id;
	private String role;
	private Boolean active;
}
