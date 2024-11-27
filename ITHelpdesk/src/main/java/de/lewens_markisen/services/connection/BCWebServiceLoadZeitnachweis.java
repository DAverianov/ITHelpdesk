package de.lewens_markisen.services.connection;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.nio.file.Path;

import org.springframework.stereotype.Component;
import de.lewens_markisen.bc_reports.BcReportParser;
import de.lewens_markisen.bc_reports.BcReportZeitNachweisDateDescriptionList;
import de.lewens_markisen.bc_reports.BcReportZeitNachweisKSaldoList;
import de.lewens_markisen.bc_reports.BcReportZeitnachweisPerson;
import de.lewens_markisen.domain.local_db.person.Person;
import de.lewens_markisen.domain.local_db.time_register_event.PersonInBcReport;
import de.lewens_markisen.person.PersonService;
import de.lewens_markisen.storage.StorageService;
import de.lewens_markisen.timeRegisterEvent.PersonInBcReportService;
import de.lewens_markisen.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class BCWebServiceLoadZeitnachweis {
	private final PersonService personService;
	private final BcReportParser bcReportParser;
	private final PersonInBcReportService personInBcReportService;
	private final StorageService storageService;

	private final String FILE_ZEITNACHWEIS_MITARBEITER = "ZeitnachweisMitarbeiter";

	public List<PersonInBcReport> loadBCZeitnachweis() {

		List<PersonInBcReport> personsInBcRep = new ArrayList<PersonInBcReport>();

		String mask = FILE_ZEITNACHWEIS_MITARBEITER + ".*\\.xml";

		//@formatter:off
		storageService.loadAll()
			.filter(path -> path.getFileName().toString().matches(mask))
			.forEach(path -> {
				personsInBcRep.addAll(loadBCZeitnachweisFromFile(storageService.load(path.getFileName().toString())));
			});
		//@formatter:on

		return personsInBcRep;
	}

	public List<PersonInBcReport> loadBCZeitnachweisFromFile(Path path) {

		List<PersonInBcReport> personsInBcRep = new ArrayList<PersonInBcReport>();

		List<BcReportZeitnachweisPerson> personsXml = bcReportParser.parse(path.toString());
		for (BcReportZeitnachweisPerson personXml : personsXml) {

			String bcCode = personXml.getAttribute().get("AZ_Person__Code");
			Optional<Person> personOpt = personService.findByBcCode(bcCode);
			if (personOpt.isEmpty()) {
				log.debug("Person with BC Code " + bcCode + " wasnt found!");
				continue;
			}
			LocalDate month = DateUtils.readDateFromString(personXml.getAttribute().get("gtxtPeriodenText"), "dd.MM.yy");

			//@formatter:off
			PersonInBcReport personInBcReport = new PersonInBcReport();
			personInBcReport.setPerson(personOpt.get());
			personInBcReport.setMonth(month);
			personInBcReport.setAttribute(personXml.getAttribute());
			personInBcReport.setDateTable(BcReportZeitNachweisDateDescriptionList.builder()
					.dateTable(personXml.getDateDescription())
					.build());
			personInBcReport.setSaldo(BcReportZeitNachweisKSaldoList.builder()
					.saldoList(personXml.getSaldo())
					.build());
			//@formatter:on

			personsInBcRep.add(personInBcReportService.save(personInBcReport));
		}
		deleteFile(path);
		log.info("File "+path+" was downloaded and deleted.");
		return personsInBcRep;
	}

	private void deleteFile(Path fileWithReport) {
		try {
			Files.delete(fileWithReport);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
