package de.lewens_markisen.security;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LewensportalRepositoryTest {
	@Autowired
	private LewensportalRepository rep;
	
	@Test
	void findByName_test() {
		Optional<LssUser> userOpt = rep.findUserByName("test");
		assertTrue(userOpt.isPresent());
	}

}
