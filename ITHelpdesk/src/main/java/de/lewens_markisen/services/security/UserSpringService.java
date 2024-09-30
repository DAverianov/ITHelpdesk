package de.lewens_markisen.services.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.security.Authority;
import de.lewens_markisen.domain.security.UserSpring;
import de.lewens_markisen.repository.security.AuthorityRepository;
import de.lewens_markisen.repository.security.UserSpringRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserSpringService {
	private final String USERROLE = "ROLE_USER";
	private final AuthorityRepository authorityRepository;
    private final UserSpringRepository userRepository;
//	private final PasswordEncoder passwordEncoder;

	public Optional<UserSpring> getUserByName(String username) {
		return userRepository.findByUsername(username);
	}

	public UserSpring createUser(String username, String password) {
		Optional<Authority> userRoleOpt = authorityRepository.findByRole(USERROLE);
		if (userRoleOpt.isEmpty()) {
			throw new UsernameNotFoundException("Role USER not found!");
		}
		//@formatter:off
		UserSpring user = UserSpring.builder()
				.username(username)
				.password(password)
				.authority(userRoleOpt.get())
				.build();
		//@formatter:on
		return userRepository.save(user);
	}

	public Optional<UserSpring> findByName(String userName) {
		return userRepository.findByUsername(userName);
	}

}
