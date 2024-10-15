package de.lewens_markisen.security;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class LewensportalRepository {

	private final JdbcTemplate jdbcTemplate;

	public LewensportalRepository(@Qualifier("lewensportalJdbcTemplate") JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	public Optional<LssUser> findUserByName(String name) {

		try {
			//@formatter:off
			Map<String, Object> dbRow = jdbcTemplate.queryForMap(
			"SELECT user.username, user_password.algorithm, user_password.salt, user_password.password " + 
			"FROM user " +
			"LEFT OUTER JOIN user_password" + " ON (user_password.user_id = user.id) " +
			"WHERE user.status = 1 and user.username = '" + name +"'" +
			"ORDER BY user_password.created_at desc");
			//@formatter:on
			if (dbRow.size() == 0) {
				return Optional.empty();
			} else {
				//@formatter:off
				return Optional.of(LssUser.builder()
						.username(name)
						.algorithm((String) dbRow.get("algorithm"))
						.salt((String) dbRow.get("salt"))
						.password((String) dbRow.get("password"))
						.build());
				//@formatter:on
			}
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	public Optional<String> getBcCodeByUsername(String username) {

		try {
			//@formatter:off
			Map<String, Object> dbRow = jdbcTemplate.queryForMap(
			"SELECT user.username, profile.bccode " +
			"FROM user " +
			"LEFT OUTER JOIN profile ON (profile.user_id = user.id)" + 
			"WHERE user.username = '" + username + "'");
			//@formatter:on
			if (dbRow.size() == 0) {
				return Optional.empty();
			} else {
				String bcCode = Integer.toString((Integer) dbRow.get("bccode"));
				if (bcCode == null || bcCode.isBlank()) {
					return Optional.empty();
				} else {
					return Optional.of(bcCode);
				}
			}
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	public Optional<Map<String, Object>> getProfileAttributsByUserId(String username) {

		try {
			//@formatter:off
			Map<String, Object> dbRow = jdbcTemplate.queryForMap(
			"SELECT * FROM profile " + 
			"INNER JOIN user ON profile.user_id = user.id " + 
			"WHERE user.username = '" + username + "'");
			//@formatter:on

			if (dbRow.size() == 0) {
				return Optional.empty();
			} else {
				return Optional.ofNullable(dbRow);
			}
		} catch (Exception e) {
			return Optional.empty();
		}
	}
}
