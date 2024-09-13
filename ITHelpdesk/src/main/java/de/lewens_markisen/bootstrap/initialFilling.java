package de.lewens_markisen.bootstrap;

import lombok.RequiredArgsConstructor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import de.lewens_markisen.access.Access;
import de.lewens_markisen.access.AccessRepository;
import de.lewens_markisen.person.Person;
import de.lewens_markisen.person.PersonRepository;
import de.lewens_markisen.utils.FileOperations;

@RequiredArgsConstructor
@Component
public class initialFilling implements CommandLineRunner {

	private static final String FILE_PERSON = "initialFilling/person.csv";
	private final PersonRepository personRepository;
	private final AccessRepository accessRepository;

	@Override
	public void run(String... args) {
		loadPersonData();
		loadAccesses();
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
