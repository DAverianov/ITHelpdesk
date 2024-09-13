package de.lewens_markisen.timeRegisterEvent;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TimeRegisterEventRepositoryTest {

	@Autowired
	private TimeRegisterEventRepository timeRegisterEventRepository;

	@Test
	public void findAllByPersonWithoutDubl_whenRequest_thenAnser() {
		List<TimeRegisterEvent> times = timeRegisterEventRepository.findAllByPersonWithoutDubl(27l);
		assertThat(times).isNotNull();
	}

}
