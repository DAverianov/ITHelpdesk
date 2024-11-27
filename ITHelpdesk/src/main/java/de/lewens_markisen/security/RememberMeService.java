package de.lewens_markisen.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RememberMeService {
	@Autowired
	private final JdbcTemplate jdbcTemplate;

	public void deleteAllRecords() {
		jdbcTemplate.update("DELETE FROM persistent_logins");
	}

}
