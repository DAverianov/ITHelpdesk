package de.lewens_markisen.services.connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import de.lewens_markisen.domain.local_db.time_register_event.PersonInBcReport;
import de.lewens_markisen.storage.StorageService;

@ActiveProfiles("test")
@SpringBootTest()
class BCWebServiceLoadZeitnachweisTest {
	private final String FILE_WITH_REPORT = "ZeitnachweisMitarbeiter 2409.xml";

	@Autowired
	private BCWebServiceLoadZeitnachweis bcWebServiceLoadZeitnachweis;

	@MockBean
	private StorageService storageService;


	@Test
	void loadBCZeitnachweis_whenLoad_thenReceive() throws IOException {
	    String path = "/src/test/resources";
	    
	    Path originalPath = new File("."+path+"/testFiles", FILE_WITH_REPORT).toPath();
	    Path copied = Paths.get("src/test/resources/"+FILE_WITH_REPORT);
	    Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);

	    File file = new File("."+path, FILE_WITH_REPORT);

		given(this.storageService.load(anyString())).willReturn(file.toPath());

		List<PersonInBcReport> personsInBcRep = bcWebServiceLoadZeitnachweis.loadBCZeitnachweis();

//		assertThat(personsInBcRep).isNotEmpty();
//		PersonInBcReport personInBcRep = personsInBcRep.get(0);
//		assertThat(personInBcRep.getAttribute().get("AZ_Person__Code")).isNotNull();
//		assertThat(personInBcRep.getAttribute().get("AZ_Person__Name")).isNotNull();
		
	}

}
