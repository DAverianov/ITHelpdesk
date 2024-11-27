package de.lewens_markisen.services.connection;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import de.lewens_markisen.bootstrap.PersonCodeName;
import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.person.PersonService;
import de.lewens_markisen.services.connection.jsonModele.PersonBcJson;
import de.lewens_markisen.services.connection.jsonModele.PersonBcJsonList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class BCWebServiceLoadPerson {
	private final ConnectionBC connectionBC;
	private final PersonService personService;

	public void loadPersonFromBC() {
		Optional<List<Person>> personsBCOpt = readPersonsFromBC();
		// @formatter:off
		if (personsBCOpt.isPresent()) {
			personsBCOpt.get().stream()
				.filter(p -> p.getBcCode().length() > 0	& p.getBcCode().length() <= 4)
				.forEach(p -> {
					List<Person> personList = personService.findAllByBcCode(p.getBcCode());
					if (personList.size() == 0 && p.getFiringDate().equals(Person.getDefaultFiringDate())) {
						createPerson(p);
					} else if (personList.size() == 1) {
						correctPerson(personList.get(0), p);
					}
				});
		}
		// @formatter:on
	}

	private void createPerson(Person p) {
		LocalDate emptyDate = LocalDate.of(1, 1, 1);
		if (p.getFiringDate().equals(emptyDate) || p
				.getFiringDate().isAfter(LocalDate.now())) {
			personService.save(p);
			log.debug("loaded Person: " + p.getName() + " "
					+ p.getBcCode());
		}
	}

	private void correctPerson(Person personForSave, Person personFromBC) {
		if (!personFromBC.getName().equals(personForSave.getName())
				|| !personFromBC.getDateOfBirthday().equals(
						personForSave.getDateOfBirthday())
				|| !personFromBC.getHiringDate().equals(
						personForSave.getHiringDate())
				|| !personFromBC.getFiringDate().equals(
						personForSave.getFiringDate())) {
			personForSave.setName(personFromBC.getName());
			personForSave.setDateOfBirthday(
					personFromBC.getDateOfBirthday());
			personForSave.setHiringDate(personFromBC.getHiringDate());
			personForSave.setFiringDate(personFromBC.getFiringDate());
			personService.save(personForSave);
			log.debug("loaded new Name for Person: "
					+ personFromBC.getName() + " " + personFromBC.getBcCode());
		}
	}

	public Optional<List<Person>> readPersonsFromBC() {
		Optional<List<Person>> result = Optional.empty();
		try {
			String requestZeitpunktposten = connectionBC.getUrl() + "/"
					+ connectionBC.getWsPersonenkarte()
//					+ "?$filter=Konzernaustritt_SOC%20eq%200001-01-01%20&%20&%20$select=Code,Name,Geburtsdatum,Konzerneintritt,Konzernaustritt_SOC,Benutzer";
					+ "?$select=Code,Name,Geburtsdatum,Konzerneintritt,Konzernaustritt_SOC,Benutzer";
			Optional<String> anserOpt = connectionBC
					.createGETRequest(requestZeitpunktposten);
			if (anserOpt.isPresent()) {
				result = readPersonFromJson(anserOpt.get());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private List<RestApiQueryFilter> createFilterKonzernAustritt() {
		List<RestApiQueryFilter> filter = new ArrayList<RestApiQueryFilter>();
		// @formatter:off
		filter.add(RestApiQueryFilter.builder().attribute("Konzernaustritt_SOC")
				.comparisonType("eq").value("0001-01-01").stringAttribute(true)
				.build());
		// @formatter:on
		return filter;
	}

	private Optional<List<Person>> readPersonFromJson(String anser) {
		Optional<List<PersonCodeName>> result = Optional.empty();
		// @formatter:off
		try {
			ObjectMapper objectMapper = JsonMapper.builder()
					.configure(
							DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
							false)
					.build();
			PersonBcJsonList l;
			l = objectMapper.readerFor(PersonBcJsonList.class).readValue(anser);
			Optional<List<Person>> convertToPerson = convertJsonToPerson(
					l.getValue());
			if (convertToPerson.isPresent()) {
				return convertToPerson;
			} else {
				return Optional.empty();
			}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		// @formatter:on
		return Optional.empty();
	}

	private Optional<List<Person>> convertJsonToPerson(
			List<PersonBcJson> value) {
		if (value.size() == 0) {
			return Optional.empty();
		}
		List<Person> persons = new ArrayList<Person>();
		// @formatter:0ff
		value.stream().filter(
				p -> p.getKonzernaustritt_SOC().isBefore(LocalDate.now()))
				.forEach(p -> persons.add(Person.builder().name(p.getName())
						.bcCode(p.getCode()).dateOfBirthday(p.getGeburtsdatum())
						.hiringDate(p.getKonzerneintritt())
						.firingDate(p.getKonzernaustritt_SOC()).build()));
		// @formatter:on
		return Optional.of(persons);
	}

}
