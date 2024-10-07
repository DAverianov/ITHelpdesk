package de.lewens_markisen.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import de.lewens_markisen.domain.security.Role;
import de.lewens_markisen.domain.security.UserSpring;
import de.lewens_markisen.person.Person;
import de.lewens_markisen.repository.AccessRepository;
import de.lewens_markisen.repository.PersonRepository;
import de.lewens_markisen.repository.security.AuthorityRepository;
import de.lewens_markisen.repository.security.UserSpringRepository;
import de.lewens_markisen.security.RoleService;
import de.lewens_markisen.security.UserSpringService;
import de.lewens_markisen.utils.FileOperations;

@Slf4j
@RequiredArgsConstructor
@Component
public class initialFilling implements CommandLineRunner {

	private static final String FILE_PERSON = "initialFilling/person.csv";
	private final PersonRepository personRepository;
	private final AccessRepository accessRepository;
	private final RoleService roleService;
	private final AuthorityRepository authorityRepository;
	private final UserSpringRepository userRepository;
	private final UserSpringService userSpringService;
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
		// person auths
		Authority createPerson = authorityRepository.save(Authority.builder().permission("person.create").build());
		Authority readPerson = authorityRepository.save(Authority.builder().permission("person.read").build());
		Authority updatePerson = authorityRepository.save(Authority.builder().permission("person.update").build());
		Authority deletePerson = authorityRepository.save(Authority.builder().permission("person.delete").build());
		Authority loadPerson = authorityRepository.save(Authority.builder().permission("person.load").build());

		// user auths
		Authority createUser = authorityRepository.save(Authority.builder().permission("user.create").build());
		Authority readUser = authorityRepository.save(Authority.builder().permission("user.read").build());
		Authority updateUser = authorityRepository.save(Authority.builder().permission("user.update").build());
		Authority deleteUser = authorityRepository.save(Authority.builder().permission("user.delete").build());

		// access auths
		Authority createAccess = authorityRepository.save(Authority.builder().permission("access.create").build());
		Authority readAccess = authorityRepository.save(Authority.builder().permission("access.read").build());
		Authority updateAccess = authorityRepository.save(Authority.builder().permission("access.update").build());
		Authority deleteAccess = authorityRepository.save(Authority.builder().permission("access.delete").build());
		
		// timeReport auths
		Authority runTimeReport = authorityRepository.save(Authority.builder().permission("timeReport.run").build());
		Authority runPersonTimeReport = authorityRepository.save(Authority.builder().permission("person.timeReport.run").build());

		Role adminRole = roleService.saveIfNotExist(Role.builder().name("ADMIN").build());
		Role userRole = roleService.saveIfNotExist(Role.builder().name("USER").build());
		Role personDepartmentRole = roleService.saveIfNotExist(Role.builder().name("PERSON_DEPARTMENT").build());

        //@formatter:off
		adminRole.setAuthorities(new HashSet<>(
				Set.of(createPerson, readPerson, updatePerson, deletePerson, loadPerson, 
						createUser, readUser, updateUser, deleteUser, 
						createAccess, readAccess, updateAccess, deleteAccess,
						runTimeReport, runPersonTimeReport)));

        userRole.setAuthorities(new HashSet<>(Set.of(runTimeReport)));
        
        personDepartmentRole.setAuthorities(new HashSet<>(Set.of(readPerson, loadPerson, runTimeReport, runPersonTimeReport)));
        roleService.saveAll(Arrays.asList(adminRole, userRole, personDepartmentRole));
        //@formatter:on

		//@formatter:off
		UserSpring user = UserSpring.builder()
			.username("dmytroaverianov")
			.password("1")
			.role(adminRole)
			.build();
		userSpringService.saveIfNotExist(user);

        // user Admin for Tests
		user = UserSpring.builder()
                .username("spring")
                .password(passwordEncoder.encode("guru"))
                .role(adminRole)
                .build();
		userSpringService.saveIfNotExist(user);

        // user User for Tests
		user = UserSpring.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .role(userRole)
                .build();
		userSpringService.saveIfNotExist(user);

        // user Personalabteilung for Tests
		user = UserSpring.builder()
                .username("personalabteilung")
                .password(passwordEncoder.encode("Alfa Zentavra 00!"))
                .role(personDepartmentRole)
                .build();
		userSpringService.saveIfNotExist(user);

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
