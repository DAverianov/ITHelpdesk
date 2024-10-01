package de.lewens_markisen.services.security;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.security.Authority;
import de.lewens_markisen.domain.security.UserSpring;
import de.lewens_markisen.repository.security.AuthorityRepository;
import de.lewens_markisen.repository.security.UserSpringRepository;
import de.lewens_markisen.utils.StringUtilsLSS;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserSpringServiceImpl implements UserSpringService {
	private final String USERROLE = "ROLE_USER";
	private final AuthorityRepository authorityRepository;
	private final UserSpringRepository userSpringRepository;
//	private final PasswordEncoder passwordEncoder;

	public Optional<UserSpring> getUserByName(String username) {
		return userSpringRepository.findFirstByUsername(username);
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
		return userSpringRepository.save(user);
	}

	public Optional<UserSpring> findByName(String userName) {
		return userSpringRepository.findFirstByUsername(userName);
	}

	public Optional<UserSpring> findById(Integer id) {
		return userSpringRepository.findById(id);
	}

	public Page<UserSpring> findAll(Pageable pageable) {
		return userSpringRepository.findAll(pageable);
	}

	public UserSpring updateUser(UserSpring user) {
		userSpringRepository.findById(user.getId());
		return userSpringRepository.save(user);
	}

	@Override
	public void delete(UserSpring user) {
		userSpringRepository.delete(user);
	}

	public void rewriteUsernames() {
		List<UserSpring> users = userSpringRepository.findAll();
		users.stream().forEach(user -> {
			user.setUsername(convertNameToLowCase( user.getUsername()));
			userSpringRepository.save(user);
		});
	}

	@Override
	public String convertNameToLowCase(String username) {
		return StringUtilsLSS.replaceUmlauts(StringUtils.lowerCase(StringUtils.deleteWhitespace(username)));
	}

}
