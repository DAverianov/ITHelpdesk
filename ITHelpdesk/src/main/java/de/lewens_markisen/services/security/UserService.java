package de.lewens_markisen.services.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.security.Authority;
import de.lewens_markisen.domain.security.User;
import de.lewens_markisen.repository.security.AuthorityRepository;
import de.lewens_markisen.repository.security.UserRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserService {
	private final String USERROLE = "USER";
	private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public Optional<User> getUserByName(String username) {
		return userRepository.findByUsername(username);
	}

	public User createUser(String username, String password) {
		Optional<Authority> userRoleOpt = authorityRepository.findByRole(USERROLE);
		if (userRoleOpt.isEmpty()) {
			throw new UsernameNotFoundException("Role USER not found!");
		}
		//@formatter:off
		User user = User.builder()
				.username(username)
				.password(passwordEncoder.encode(password))
				.authority(userRoleOpt.get())
				.build();
		//@formatter:on
		return userRepository.save(user);
	}

}
