package de.lewens_markisen.services.connection;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.gson.Gson;

import de.lewens_markisen.bc_reports.BcReportParser;
import de.lewens_markisen.bc_reports.BcReportZeitnachweisPerson;
import de.lewens_markisen.bootstrap.PersonCodeName;
import de.lewens_markisen.domain.local_db.Person;
import de.lewens_markisen.domain.local_db.time_register_event.PersonInBcReport;
import de.lewens_markisen.domain.local_db.time_register_event.TimeRegisterEvent;
import de.lewens_markisen.person.PersonService;
import de.lewens_markisen.services.connection.jsonModele.PersonBcJson;
import de.lewens_markisen.services.connection.jsonModele.PersonBcJsonList;
import de.lewens_markisen.services.connection.jsonModele.TimeRegisterEventJson;
import de.lewens_markisen.services.connection.jsonModele.TimeRegisterEventJsonList;
import de.lewens_markisen.timeRegisterEvent.PersonInBcReportService;
import de.lewens_markisen.timeReport.PeriodReport;
import de.lewens_markisen.utils.FileOperations;
import de.lewens_markisen.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class BCWebService {
	private final ConnectionBC connectionBC;
	private final PersonService personService;
	private final BcReportParser bcReportParser;
	private final PersonInBcReportService personInBcReportService;
	
	@Value("${import.reports.zeitnachweismitarbeiter}")
	private String fileZeitnachweisMitarbeiter;

	public Optional<List<TimeRegisterEvent>> readTimeRegisterEventsFromBC(Person person, PeriodReport period) {
		Optional<List<TimeRegisterEvent>> result = Optional.empty();
		try {
			log.debug("BC read from "+connectionBC.getWsZeitpunktposten());
			String requestZeitpunktposten = connectionBC.getUrl() + "/" + connectionBC.getWsZeitpunktposten()
					+ connectionBC.getFilter(createFilterPersonsTime(person.getBcCode(), period));
			Optional<String> anserOpt = connectionBC.createGETRequest(requestZeitpunktposten);
			if (anserOpt.isPresent()) {
				result = readTimeRegisterEventsFromJson(anserOpt.get());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private List<RestApiQueryFilter> createFilterPersonsTime(String bcCode, PeriodReport period) {
		List<RestApiQueryFilter> filter = new ArrayList<RestApiQueryFilter>();
		//@formatter:off
		filter.add(RestApiQueryFilter.builder()
				.attribute("Person")
				.comparisonType("eq")
				.value(bcCode)
				.stringAttribute(true)
				.build());
		filter.add(RestApiQueryFilter.builder()
				.attribute("Von_Datum")
				.comparisonType("ge")
				.value(period.getStart().toString())
				.stringAttribute(false)
				.build());
		filter.add(RestApiQueryFilter.builder()
				.attribute("Von_Datum")
				.comparisonType("le")
				.value(period.getEnd().toString())
				.stringAttribute(false)
				.build());
		//@formatter:on
		return filter;
	}

	private Optional<List<TimeRegisterEvent>> readTimeRegisterEventsFromJson(String anser) {
		Optional<List<TimeRegisterEvent>> result = Optional.empty();
		//@formatter:off
		try {
		    ObjectMapper objectMapper = JsonMapper.builder()
		    		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
		    		.build();
		    TimeRegisterEventJsonList l;
			l = objectMapper.readerFor(TimeRegisterEventJsonList.class)
					.readValue(anser);
			Optional<List<TimeRegisterEvent>> convertToTimeRegisterEvent = convertToTimeRegisterEvent(l.getValue());
			if (convertToTimeRegisterEvent.isPresent()) {
				return convertToTimeRegisterEvent;
			}
			else {
				return Optional.empty();
			}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	    //@formatter:on
		return result;
	}
	
	public Optional<List<TimeRegisterEvent>> convertToTimeRegisterEvent(List<TimeRegisterEventJson> value) {
		if (value.size() == 0) {
			return Optional.empty();
		}
		Optional<Person> personOpt = personService.findOrCreate(value.get(0).getPerson(), value.get(0).getName());
		if (personOpt.isEmpty()) {
			return Optional.empty();
		}
		List<TimeRegisterEvent> events = new ArrayList<TimeRegisterEvent>();
		// @formatter:0ff
		value.stream().forEach(tR -> events.add(TimeRegisterEvent.builder()
				.person(personOpt.get())
				.eventDate(tR.getEventDate())
				.startTime(tR.getStartDate())
				.endTime(tR.getEndDate())
				.build()));
		//@formatter:on
		return Optional.of(compoundDublRecords(events));
	}

	public List<TimeRegisterEvent> compoundDublRecords(List<TimeRegisterEvent> events) {
		List<TimeRegisterEvent> eventsCompound = new ArrayList<TimeRegisterEvent>();
		//@formatter:off
		// 1. Manual with Start and End Time
		events.stream()
			.filter(ev -> ev.getStartTime()!="" && ev.getEndTime()!="")
			.forEach(ev -> {
				eventsCompound.add(TimeRegisterEvent.builder()
					.person(ev.getPerson())
					.eventDate(ev.getEventDate())
					.startTime(ev.getStartTime())
					.endTime(ev.getEndTime())
					.build());
		});
		// 2. Auto separately Start and End time
		events.stream()
		.filter(ev -> ev.getStartTime()!="" && ev.getEndTime()=="")
		.forEach(ev -> {
			eventsCompound.add(TimeRegisterEvent.builder()
					.person(ev.getPerson())
					.eventDate(ev.getEventDate())
					.startTime(ev.getStartTime())
					.endTime(findEndTime(events, ev, ev.getStartTime()))
					.build());
		});
		//@formatter:on
		return eventsCompound;
	}

	private String findEndTime(List<TimeRegisterEvent> events, TimeRegisterEvent currentEvent, String startTime) {
		String result = "";
		for (TimeRegisterEvent ev : events) {
			//@formatter:off
			if (ev.getPerson().equals(currentEvent.getPerson()) 
					&& ev.getEventDate().equals(currentEvent.getEventDate())
					&& ev.getStartTime().equals("")
					&& TimeUtils.convertTimeToInt(ev.getEndTime()) > TimeUtils.convertTimeToInt(currentEvent.getStartTime()) ) {
				result = ev.getEndTime();
				break;
			}
			//@formatter:on
		}
		return result;
	}

	public void loadPersonFromBC() {
		Optional<List<Person>> personsBCOpt = readPersonsFromBC();
		//@formatter:off
		if (personsBCOpt.isPresent()) {
			personsBCOpt.get().stream()
				.forEach(p -> {
					Optional<Person> personFetchOpt = personService.findByBcCode(p.getBcCode());
					if (personFetchOpt.isPresent()) { // TODO control Name
					}
					else {
						personService.save(p);
						log.debug("loaded Person: " + p.getName()+" "+p.getBcCode());
					}
				});
		}
		//@formatter:on
	}

	public Optional<List<Person>> readPersonsFromBC() {
		Optional<List<Person>> result = Optional.empty();
		try {
			String requestZeitpunktposten = connectionBC.getUrl() + "/" 
				+ connectionBC.getWsPersonenkarte()
				+ "?$filter=Konzernaustritt_SOC%20eq%200001-01-01%20&%20$select=Code,Name,Geburtsdatum,Konzerneintritt,Konzernaustritt_SOC,Benutzer";
			Optional<String> anserOpt = connectionBC.createGETRequest(requestZeitpunktposten);
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
		//@formatter:off
		filter.add(RestApiQueryFilter.builder()
				.attribute("Konzernaustritt_SOC")
				.comparisonType("eq")
				.value("0001-01-01")
				.stringAttribute(true)
				.build());
		//@formatter:on
		return filter;
	}

	private Optional<List<Person>> readPersonFromJson(String anser) {
		Optional<List<PersonCodeName>> result = Optional.empty();
		//@formatter:off
		try {
		    ObjectMapper objectMapper = JsonMapper.builder()
		    		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
		    		.build();
		    PersonBcJsonList l;
			l = objectMapper.readerFor(PersonBcJsonList.class).readValue(anser);
			Optional<List<Person>> convertToPerson = convertJsonToPerson(l.getValue());
			if (convertToPerson.isPresent()) {
				return convertToPerson;
			}
			else {
				return Optional.empty();
			}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	    //@formatter:on
		return Optional.empty();
	}

	private Optional<List<Person>> convertJsonToPerson(List<PersonBcJson> value) {
		if (value.size() == 0) {
			return Optional.empty();
		}
		List<Person> persons = new ArrayList<Person>();
		// @formatter:0ff
		value.stream()
			.filter(p -> p.getKonzernaustritt_SOC().isBefore(LocalDate.now()))
			.forEach(p -> persons.add(
				Person.builder()
					.name(p.getName())
					.bcCode(p.getCode())
					.build()));
		//@formatter:on
		return Optional.of(persons);
	}

	public void loadBCZeitnachweis() {
		
		List<BcReportZeitnachweisPerson> personsXml = bcReportParser.parse(getFilePathWithReport());
		for (BcReportZeitnachweisPerson personXml: personsXml) {
			
			String bcCode = personXml.getAttribute().get("AZ_Person__Code");
			Optional<Person> personOpt = personService.findByBcCode(bcCode);
			if (personOpt.isEmpty()) {
				log.debug("Person with BC Code "+bcCode+" wasnt found!");
				continue;
			}
			LocalDate month = readDateFromString(personXml.getAttribute().get("gtxtPeriodenText"));
			
			PersonInBcReport personInBcReport = new PersonInBcReport();
			personInBcReport.setPerson(personOpt.get());
			personInBcReport.setMonth(month);
			personInBcReport.setAttribute(personXml.getAttribute());
			
			personInBcReportService.save(personInBcReport);
		}
		
	}

	private String getFilePathWithReport() {
		FileOperations fileOp = new FileOperations();
		File file = fileOp.getFileFromResources(fileZeitnachweisMitarbeiter);
		return file.getAbsolutePath();
	}

	private LocalDate readDateFromString(String reportMonth) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
		return LocalDate.parse(reportMonth.substring(0, 8), formatter);
	}

}
