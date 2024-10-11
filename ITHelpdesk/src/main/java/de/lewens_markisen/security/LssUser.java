package de.lewens_markisen.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class LssUser {
	private String username;
	private String algorithm;
	private String salt;
	private String password;
}
