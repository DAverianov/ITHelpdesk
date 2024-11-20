package de.lewens_markisen.email;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.lewens_markisen.domain.local_db.email.EmailAccountLss;

public interface EmailAccountService {
	public Long count();
	public EmailAccountLss save(EmailAccountLss account);
	public Page<EmailAccountLss> findAll(Pageable pageable);
	public Optional<EmailAccountLss> findById(Long id);
	public EmailAccountLss update(EmailAccountLss account);
}
