package de.lewens_markisen.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import de.lewens_markisen.access.Access;
import de.lewens_markisen.domain.security.Authority;
import de.lewens_markisen.domain.security.UserSpring;
import de.lewens_markisen.person.Person;
import de.lewens_markisen.repository.AccessRepository;
import de.lewens_markisen.repository.PersonRepository;
import de.lewens_markisen.repository.security.AuthorityRepository;
import de.lewens_markisen.repository.security.UserSpringRepository;
import de.lewens_markisen.utils.FileOperations;

@Slf4j
@RequiredArgsConstructor
@Component
public class initialFilling implements CommandLineRunner {

	private static final String FILE_PERSON = "initialFilling/person.csv";
	private final PersonRepository personRepository;
	private final AccessRepository accessRepository;
	private final AuthorityRepository authorityRepository;
	private final UserSpringRepository userRepository;
    private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) {
		loadSecurityData();
		loadAccesses();
		loadPersonData();
	}

	private void loadSecurityData() {
		if (authorityRepository.count() > 0) {
			return;
		}
		Authority adminRole = authorityRepository.save(Authority.builder().role("ROLE_ADMIN").build());
		Authority userRole = authorityRepository.save(Authority.builder().role("ROLE_USER").build());
		Authority userPersonalAbteilungRole = authorityRepository.save(Authority.builder().role("ROLE_PERSONALABTEILUNG").build());

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

        // user Admin for Tests
		user = UserSpring.builder()
                .username("spring")
                .password(passwordEncoder.encode("guru"))
                .build();
		auth.add(adminRole);
		user.setAuthorities(auth);
		userRepository.save(user);

        // user User for Tests
		user = UserSpring.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .build();
		auth.add(userRole);
		user.setAuthorities(auth);
		userRepository.save(user);

        // user Personalabteilung for Tests
		user = UserSpring.builder()
                .username("personalabteilung")
                .password(passwordEncoder.encode("Alfa Zentavra 00!"))
                .build();
		auth.add(userPersonalAbteilungRole);
		user.setAuthorities(auth);
		userRepository.save(user);

 		//@formatter:on
		log.debug("Users Loaded: " + userRepository.count());
	}

	private void loadAccesses() {
		if (accessRepository.count() == 0) {
			//@formatter:off
			Access access = Access.builder()
						.name("BC develop")
						.url("http://lss-bc-app.lss.local:7048/BC21-LEWENS/ODataV4/Company('LSS%20Produktiv')")
						.domain("LSS.local")
						.user("DmytroAverianov")
						.password("11")
						.description("autocreated record")
						.build();
			accessRepository.save(access);
			//@formater:on
		}
	}

	private void loadPersonData() {
		if (personRepository.count() == 0) {
			List<PersonCodeName> personCodeName = readPersonFile();
			//@formatter:off
			personCodeName.stream().forEach(p -> {
				Person pers = Person.builder()
						.name(p.getName())
						.bcCode(p.getCode())
						.build();
				personRepository.save(pers);
			});
			//@formater:on
		}
	}

	private List<PersonCodeName> readPersonFile() {
		List<PersonCodeName> personCodeList = new ArrayList<PersonCodeName>();
		CSVParser csvParser = new CSVParserBuilder().withSeparator(',').build();
		FileOperations fo = new FileOperations();
		//@formatter:off
		try (CSVReader reader = new CSVReaderBuilder(new FileReader(fo.getFileFromResources(FILE_PERSON)))
				.withCSVParser(csvParser) // custom CSV parser
				.withSkipLines(1) // skip the first line, header info
				.build()) {
			List<String[]> r = reader.readAll();
			r.forEach(x -> personCodeList.add(new PersonCodeName(x[0], x[1])));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CsvException e) {
			e.printStackTrace();
		}
		//@formater:on
		return personCodeList;
	}

}
