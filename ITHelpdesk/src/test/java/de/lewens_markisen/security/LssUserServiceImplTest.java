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
	
	private final String TEST_PASSWORD = "Fomalgaut1";
	private final String SALT = "f7f2bcd0-22f3-4268-a797-2497e8b33bc5";
	private final String WHIRLPOOL = "b81f56917fc9ab9328ac6b5d7545a1b6c85ae6ed8f8ea22b9fec1a1e220b407fb760f39e4ef9479e2e1fa415ec7ace2e0a77f81a111661ec3b5b8724baf1b309";
	private final String HASH_PASSWORD = "cc4c6da6aa7068c774cf209feef5c79ed20167cf1136ad210bcfb7240965f33e429c30b697ce4ca0434e0f0d017e0bc427e98a5e0d8bff5453773689260b1be2";

	@Test
	void hash_Whirlpool() throws NoSuchAlgorithmException {
		String hash = lssUserService.hash("WHIRLPOOL", TEST_PASSWORD+SALT);
		assertNotNull(hash);
		assertEquals(WHIRLPOOL, hash);
	}
	
	@Test
	void hash_Sha512() throws NoSuchAlgorithmException {
		String hash = lssUserService.hash("SHA-512", WHIRLPOOL);
		assertNotNull(hash);
		assertEquals(HASH_PASSWORD, hash);
	}
	
	@Test
	void entUserTest() throws NoSuchAlgorithmException {
		String hash = lssUserService.getLssHashPassword(TEST_PASSWORD, SALT);
		assertNotNull(hash);
		assertEquals(HASH_PASSWORD, hash);
	}

}
