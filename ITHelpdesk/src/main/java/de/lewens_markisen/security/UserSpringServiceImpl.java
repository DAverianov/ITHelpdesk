package de.lewens_markisen.security;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.security.Role;
import de.lewens_markisen.domain.local_db.security.UserSpring;
import de.lewens_markisen.person.PersonService;
import de.lewens_markisen.repository.local.security.RoleRepository;
import de.lewens_markisen.repository.local.security.UserSpringRepository;
import de.lewens_markisen.utils.StringUtilsLSS;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserSpringServiceImpl implements UserSpringService {
	private final String USERROLE = "USER";
	private final RoleRepository roleRepository;
	private final UserSpringRepository userSpringRepository;
	private final LssUserService lssUserService;
	private final PersonService personService;

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

	public Optional<UserSpring> findByUsername(String userName) {
		return userSpringRepository.findFirstByUsername(userName);
	}

	public Optional<UserSpring> findById(Integer id) {
		return userSpringRepository.findById(id);
	}

	public Page<UserSpring> findAll(Pageable pageable) {
		return userSpringRepository.findAll(pageable);
	}

	public UserSpring saveUser(UserSpring user) {
		UserSpring userWithThatName = userSpringRepository.findByUsername(user.getUsername()).get();
		if (userWithThatName.getId() != user.getId()) {
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
			user.setUsername(StringUtilsLSS.convertNameToLowCase( user.getUsername()));
			userSpringRepository.save(user);
		});
	}

	@Override
	public UserSpring saveIfNotExist(UserSpring user) {
		Optional<UserSpring> userOpt = findByUsername(user.getUsername());
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
			return findByUsername(authName);
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

	@Override
	public UserSpring fillAttributsFromLss(UserSpring user) {
		UserSpring userWithAttr = findById(user.getId()).get();
		Optional<Map<String, Object>> attrOpt = lssUserService.getProfileAttributsByLssUser(user.getUsername());
		if (attrOpt.isEmpty()) {
			return userWithAttr;
		}
		Map<String, Object> attr = attrOpt.get();
		
		Boolean isCorrect = true;
		
		//@formatter:off
		isCorrect = isCorrect & 
				checkAttribute(userWithAttr, "firstname", attr,
				(attrMap, attrName) -> (String) attrMap.get("firstname"),
				a -> a.getFirstname(), 
				(a, firstname) -> a.setFirstname(firstname));
		isCorrect = isCorrect & 
				checkAttribute(userWithAttr, "lastname", attr, 
				(attrMap, attrName) -> (String) attrMap.get("lastname"),
				a -> a.getLastname(), 
				(a, lastname) -> a.setLastname(lastname));
		isCorrect = isCorrect & 
				checkAttribute(userWithAttr, "bccode", attr,
				(attrMap, attrName) -> Integer.toString((Integer) attr.get("bccode")),
				a -> a.getBcCode(),
				(a, bcCode) -> a.setBcCode(bcCode));
		isCorrect = isCorrect & 
				checkAttribute(userWithAttr, "email", attr,
						(attrMap, attrName) -> (String) attr.get("email"),
						a -> a.getEmail(),
						(a, email) -> a.setEmail(email));
		//@formatter:on
		
		isCorrect = isCorrect & 
				findPersonForUser(userWithAttr);
		
		if (isCorrect) {
			return userWithAttr;
		}
		else {
			return userSpringRepository.save(userWithAttr);
		}
	}

	//@formatter:off
	private Boolean checkAttribute(
			UserSpring userWithAttr, 
			String attrName, 
			Map<String, Object> attr,
			BiFunction<Map<String, Object>, String, String> getLssAttribute, 
			Function<UserSpring, String> getAttribute, 
			BiConsumer<UserSpring, String> setAttribute) {
		if (attr.containsKey(attrName)) {
			String attrValue = getLssAttribute.apply(attr, attrName);
			String attrValueUser = getAttribute.apply(userWithAttr);
			if (attrValueUser != null && attrValueUser.equals(attrValue)) {
				return true;
			}
			else {
				setAttribute.accept(userWithAttr, attrValue);
				return false;
			}
		}
		return true;
	}
	//@formatter:on

	private Boolean findPersonForUser(UserSpring userWithAttr) {
		Boolean isCorrect = true;
		if (userWithAttr.getBcCode() != null && !userWithAttr.getBcCode().isBlank()) {
			Optional<Person> personOpt = personService.findByBcCode(userWithAttr.getBcCode());
			if (personOpt.isPresent()) {
				if(userWithAttr.getPerson() != personOpt.get()) {
					isCorrect = false;
					userWithAttr.setPerson(personOpt.get());
				}
			}
		}
		return isCorrect;
	}

	@Override
	public Boolean userHasEmail() {
		Optional<UserSpring> currUserOpt = getCurrentUser();
		if (currUserOpt.isEmpty()) {
			return false;
		}
		else if (currUserOpt.get().getEmail().isBlank()) {
			return false;
		}
		else {
			return true;
		}
	}
}
