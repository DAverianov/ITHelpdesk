package de.lewens_markisen.timeRegisterEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import de.lewens_markisen.repository.local.TimeRegisterEventRepository;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TimeRegisterEventRepositoryTest {

	@Autowired
	private TimeRegisterEventRepository timeRegisterEventRepository;


}
