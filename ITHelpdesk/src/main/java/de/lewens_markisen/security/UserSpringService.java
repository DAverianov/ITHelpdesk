package de.lewens_markisen.security;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import de.lewens_markisen.domain.security.UserSpring;

public interface UserSpringService {
	public Optional<UserSpring> getUserByName(String username);
	public String getAuthenticationName();
	public Optional<UserSpring> getCurrentUser();

	public Optional<UserSpring> findByName(String userName);
	public Optional<UserSpring> findById(Integer id);
	public Page<UserSpring> findAll(Pageable pageable);

	public UserSpring createUser(String username, String password);
	public UserSpring saveUser(UserSpring user);
	public UserSpring saveIfNotExist(UserSpring user);
	public void delete(UserSpring user);

	public void rewriteUsernames();
	public String convertNameToLowCase(String username);

}
