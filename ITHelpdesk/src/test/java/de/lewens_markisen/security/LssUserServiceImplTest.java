package de.lewens_markisen.security;

import static org.junit.jupiter.api.Assertions.*;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LssUserServiceImplTest {
	@Autowired
	private LssUserService lssUserService;

	@Test
	void entcodeWhirlpool() throws NoSuchAlgorithmException {
		String password = "Fomalgaut1";
		String hash = lssUserService.entcodeWirlpool(password);
		assertNotNull(hash);
	}
	
	@Test
	void entcodeSha512() throws NoSuchAlgorithmException {
		String password = "Fomalgaut1";
		String hash = lssUserService.entcodeSha512(password);
		assertNotNull(hash);
	}

}
