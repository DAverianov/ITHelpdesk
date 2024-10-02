package de.lewens_markisen.repositories;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import de.lewens_markisen.access.Access;
import de.lewens_markisen.repository.AccessRepository;
import de.lewens_markisen.services.EncryptionService;

@SpringBootTest
@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccessRepositoryTest {
	private final String PASSWORD = "ABC123";
	@Autowired
	private EncryptionService encryptionService;
	@Autowired
	private AccessRepository accessRepository;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	public void testSaveAndStoreCreditCard() {
		//@formatter:off
		Access access = Access.builder()
				.name("passwordBC")
				.domain("LSS")
				.user("spring")
				.password(PASSWORD)
				.build();
		//@formatter:on

		Access savedAccess = accessRepository.saveAndFlush(access);

		System.out.println("Getting Acc from database: " + savedAccess.getPassword());
		System.out.println("Acc Encrypted: " + encryptionService.encrypt(PASSWORD));

		Map<String, Object> dbRow = jdbcTemplate
				.queryForMap("SELECT * FROM accesses " + "WHERE id = " + savedAccess.getId());

		String dbPassword = (String) dbRow.get("password");
		System.out.println("Password in DB: " + dbPassword);

		assertThat(savedAccess.getPassword()).isNotEqualTo(dbPassword);
		assertThat(dbPassword).isEqualTo(encryptionService.encrypt(PASSWORD));

		Access fetchedAcc = accessRepository.findById(savedAccess.getId()).get();

		assertThat(savedAccess.getPassword()).isEqualTo(fetchedAcc.getPassword());
	}
}
