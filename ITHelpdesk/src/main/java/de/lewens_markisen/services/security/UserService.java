package de.lewens_markisen.services.security;

import java.util.Optional;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.security.User;
import de.lewens_markisen.repository.security.UserRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

	public Optional<User> getUserByName(String username) {
		return userRepository.findByUsername(username);
	}

	public User createUser(String username, String password) {
		User user = User.builder().username(username).password(password).build();
		return userRepository.save(user);
	}

}
