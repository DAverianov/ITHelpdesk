package de.lewens_markisen.security;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.security.Role;
import de.lewens_markisen.domain.security.UserSpring;
import de.lewens_markisen.repository.security.RoleRepository;
import de.lewens_markisen.repository.security.UserSpringRepository;
import de.lewens_markisen.utils.StringUtilsLSS;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserSpringServiceImpl implements UserSpringService {
	private final String USERROLE = "USER";
	private final RoleRepository roleRepository;
	private final UserSpringRepository userSpringRepository;
//	private final PasswordEncoder passwordEncoder;

	public Optional<UserSpring> getUserByName(String username) {
		return userSpringRepository.findFirstByUsername(username);
	}

	public UserSpring createUser(String username, String password) {
		Optional<Role> userRoleOpt = roleRepository.findByName(USERROLE);
		if (userRoleOpt.isEmpty()) {
			throw new UsernameNotFoundException("Role USER not found!");
		}
		//@formatter:off
		UserSpring user = UserSpring.builder()
				.username(username)
				.password(password)
				.role(userRoleOpt.get())
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

	public UserSpring saveUser(UserSpring user) {
		List<UserSpring> userWithThatName = userSpringRepository.findByUsername(user.getUsername());
		Long qua = userWithThatName.stream().filter(u -> !u.getId().equals(user.getId())).collect(Collectors.counting());
		if (qua==0) {
			return userSpringRepository.save(user);
		}
		else {
			throw new BadRequestException( List.of("User with Name "+user.getUsername()+" is allready exist!"));
		}
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

	@Override
	public UserSpring saveIfNotExist(UserSpring user) {
		Optional<UserSpring> userOpt = findByName(user.getUsername());
		if (userOpt.isPresent()) {
			return userOpt.get();
		}
		else {
			return saveUser(user);
		}
	}

	@Override
	public Optional<UserSpring> getCurrentUser() {
		String authName = getAuthenticationName();
		if (authName.equals("")) {
			return Optional.empty();
		}
		else {
			return findByName(authName);
		}
	}

	@Override
	public String getAuthenticationName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			return "";
		} else {
			return auth.getName();
		}
	}

}
