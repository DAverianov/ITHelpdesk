package de.lewens_markisen.email;

import de.lewens_markisen.domain.local_db.email.EmailAccountLss;
import de.lewens_markisen.domain.local_db.email.EmailLetter;

public interface EmailLetterService {
	public EmailLetter save(EmailLetter email);
	public EmailAccountLss getServiceAccount();
	public Boolean send(EmailLetter email);
}
