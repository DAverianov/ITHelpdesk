package de.lewens_markisen.domain.security;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserSpringList {

	private List<UserSpring> users;

	@XmlElement
	public List<UserSpring> getUserSpringList() {
		if (users == null) {
			users = new ArrayList<>();
		}
		return users;
	}

}
