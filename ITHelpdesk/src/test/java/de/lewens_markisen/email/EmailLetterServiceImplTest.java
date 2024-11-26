package de.lewens_markisen.email;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.lewens_markisen.domain.local_db.email.EmailLetter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SpringBootTest
class EmailLetterServiceImplTest {
	@Autowired
	private EmailLetterService emailLetterService;

	@Test
	@Disabled
	void send_whenSend_thanOk() {
		//@formatter:off
		EmailLetter email = EmailLetter.builder()
				.sender(EmailLetter.DEFAULT_SENDER)
				.recipient("d.averianov@lewens-markisen.de")
				.subject("Hello! Test message.")
				.text("Holla! Ich bin ein Roboter!")
				.build();
		Boolean result = emailLetterService.send(email);
		assertTrue(result);
		//@formatter:on
		
	}

}
