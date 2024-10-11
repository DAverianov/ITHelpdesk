package de.lewens_markisen.log;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.lewens_markisen.repository.local.LogRepository;

@SpringBootTest
@Disabled
class LogServiceTest {
	@Autowired
	private LogRepository logRepository;
	@Autowired
	private LogService logService;

	@Test
	void deleteAllRecords_whenDelete_thanOk() {
		logService.deleteAltRecords();
		assertEquals(logRepository.count(), 0);
	}

}
