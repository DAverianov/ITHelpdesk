package de.lewens_markisen.bootstrap;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import de.lewens_markisen.domain.security.Authority;
import de.lewens_markisen.domain.security.UserSpring;
import de.lewens_markisen.repository.security.AuthorityRepository;
import de.lewens_markisen.repository.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserDataLoader implements CommandLineRunner {

	private final AuthorityRepository authorityRepository;
	private final UserRepository userRepository;

	@Override
	public void run(String... args) throws Exception {
		if (authorityRepository.count() == 0) {
			loadSecurityData();
		}
	}

	private void loadSecurityData() {
		Authority adminRole = authorityRepository.save(Authority.builder().role("ROLE_ADMIN").build());
		Authority userRole = authorityRepository.save(Authority.builder().role("ROLE_USER").build());

		//@formatter:off
		UserSpring user = UserSpring.builder()
			.username("DmytroAverianov")
			.password("1")
			.build();
		Set<Authority> auth = new HashSet<Authority>();
		auth.add(userRole);
		auth.add(adminRole);
		user.setAuthorities(auth);
		
		userRepository.save(user);
		//@formatter:on
		log.debug("Users Loaded: " + userRepository.count());
	}

}
