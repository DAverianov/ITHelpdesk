package de.lewens_markisen.email;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.local_db.email.EmailLetter;
import de.lewens_markisen.repository.local.EmailLetterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailLetterServiceImpl implements EmailLetterService{
	
	private final EmailLetterRepository emailLetterRepository;
    private final JavaMailSender javaMailSender;

	@Override
	public EmailLetter save(EmailLetter email) {
		return emailLetterRepository.save(email);
	}

	@Override
	public Boolean send(EmailLetter email) {
		if (!checkEmailLetter(email)) {
			return false;
		}
		log.debug("Email send -> "+email.getRecipient()+" -> " +email.getSubject());
		sendEmail(email);
		markEmail(email);
		return true;
	}

	@Override
	public void sendAll() {
		List<EmailLetter> letters = emailLetterRepository.findAllBySend(false);
		letters.stream().forEach(l -> send(l));
	}
	
	private void markEmail(EmailLetter email) {
        email.setSend(true);
        email.setSendDate(LocalDateTime.now());
        emailLetterRepository.save(email);
	}

	private void sendEmail(EmailLetter email) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom(email.getSender());
        message.setTo(email.getRecipient()); 
        message.setSubject(email.getSubject()); 
        message.setText(email.getText());
        javaMailSender.send(message);
	}

	private boolean checkEmailLetter(EmailLetter email) {
		if (email == null) {
			log.debug("email is null! Email dont send!");
			return false;
		}
		if (email.getSender()==null) {
			log.debug("Sender in email is null! Email dont send!");
			return false;
		}
		if (email.getRecipient()==null || email.getRecipient().isBlank()) {
			log.debug("Recipient in email is null or blank! Email dont send!");
			return false;
		}
		if (email.getSubject()==null || email.getSubject().isBlank()) {
			log.debug("Subject in email is null or blank! Email dont send!");
			return false;
		}
		return true;
	}
	
}
