package de.lewens_markisen.email;

import java.time.LocalDateTime;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.local_db.email.EmailAccountLss;
import de.lewens_markisen.domain.local_db.email.EmailLetter;
import de.lewens_markisen.repository.local.EmailLetterRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailLetterServiceImpl implements EmailLetterService{
	
	private final EmailLetterRepository emailLetterRepository;
	private final EmailAccountService emailAccountService;

	@Override
	public EmailLetter save(EmailLetter email) {
		return emailLetterRepository.save(email);
	}

	@Override
	public EmailAccountLss getServiceAccount() {
		return emailAccountService.getServiceAccount();
	}

	@Override
	public Boolean send(EmailLetter email) {
		if (!checkEmailLetter(email)) {
			return false;
		}
		sendEmail(email);
		markEmail(email);
		return true;
	}
	
	private void markEmail(EmailLetter email) {
        email.setSent(true);
        email.setSentDate(LocalDateTime.now());
        emailLetterRepository.save(email);
	}

	private void sendEmail(EmailLetter email) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom(email.getSender().getEmail());
        message.setTo(email.getRecipient()); 
        message.setSubject(email.getSubject()); 
        message.setText(email.getText());
        getJavaMailSender().send(message);
	}

	private boolean checkEmailLetter(EmailLetter email) {
		if (email == null) {
			return false;
		}
		if (email.getSender()==null) {
			return false;
		}
		if (email.getSender().getAccess()==null) {
			return false;
		}
		if (email.getRecipient()==null || email.getRecipient().isBlank()) {
			return false;
		}
		if (email.getSubject()==null || email.getSubject().isBlank()) {
			return false;
		}
		return true;
	}

	private JavaMailSender getJavaMailSender() {
		
		EmailAccountLss account = emailAccountService.getServiceAccount();
		
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost(account.getHost());
	    mailSender.setPort(account.getPort());
	    
	    mailSender.setUsername(account.getUsername());
	    mailSender.setPassword(account.getAccess().getPassword());
	    
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	    
	    return mailSender;
	}
	
}
