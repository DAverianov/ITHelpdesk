package de.lewens_markisen.email;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.lewens_markisen.domain.local_db.email.EmailAcсount;

public interface EmailAccountService {
	public Long count();
	public EmailAcсount save(EmailAcсount account);
	public Page<EmailAcсount> findAll(Pageable pageable);
	public Optional<EmailAcсount> findById(Long id);
	public EmailAcсount update(EmailAcсount account);
}
