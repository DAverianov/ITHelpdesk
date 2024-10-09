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
			Map<String, Object> dbRow = jdbcTemplate.queryForMap(
					"select user.username, user_password.algorithm, user_password.salt, user_password.password from user \r\n"
							+ "left outer join user_password\r\n" + "     on (user_password.user_id = user.id)    \r\n"
							+ "where user.status = 1 and user.username = '" + name+"'");

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
//			System.out.println(" error in jdbc query! "+e.getMessage());
//			throw e;
		}
	}
}
