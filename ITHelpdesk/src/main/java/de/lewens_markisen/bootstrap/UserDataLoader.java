package de.lewens_markisen.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import de.lewens_markisen.domain.security.Authority;
import de.lewens_markisen.domain.security.User;
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
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		if (authorityRepository.count() == 0) {
			loadSecurityData();
		}
	}

	private void loadSecurityData() {
		Authority admin = authorityRepository.save(Authority.builder().role("ADMIN").build());
		Authority userRole = authorityRepository.save(Authority.builder().role("USER").build());
		Authority customer = authorityRepository.save(Authority.builder().role("CUSTOMER").build());

		//@formatter:off
		userRepository.save(User.builder()
				.username("spring")
				.password(passwordEncoder.encode("1"))
				.authority(admin)
				.build()
				);

		userRepository.save(User.builder()
				.username("user")
				.password(passwordEncoder.encode("password"))
				.authority(userRole)
				.build());

		userRepository.save(User.builder()
				.username("scott")
				.password(passwordEncoder.encode("tiger"))
				.authority(customer)
				.build());

		//@formatter:on
		log.debug("Users Loaded: " + userRepository.count());
	}

}
