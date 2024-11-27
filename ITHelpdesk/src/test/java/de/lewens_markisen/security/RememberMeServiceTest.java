package de.lewens_markisen.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class RememberMeServiceTest {
	@Autowired
	private RememberMeService pememberMeService;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void deleteAll_whenDelete_than0Records() {
		java.util.Date date = new java.util.Date();
		java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
		jdbcTemplate.update("INSERT INTO persistent_logins (username, series, token, last_used) VALUES (?, ?, ?, ?)",
				"test", "aba86lba0p0aHx5HgfIVVA==", "+LJcnz4zmBUUzonuQI3Hlw==", timestamp);
		pememberMeService.deleteAllRecords();
		int count = jdbcTemplate.queryForObject("SELECT count(*) FROM persistent_logins", Integer.class);
		assertEquals(0, count);
	}

}
