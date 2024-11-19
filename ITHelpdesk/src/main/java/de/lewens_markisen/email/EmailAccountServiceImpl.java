package de.lewens_markisen.email;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.local_db.email.EmailAcсount;
import de.lewens_markisen.repository.local.EmailAccountRepository;
import de.lewens_markisen.services.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailAccountServiceImpl implements EmailAccountService{
	private final EmailAccountRepository emailAccountRepository;
	
	@Override
	public Long count() {
		return emailAccountRepository.count();
	}

	@Override
	public EmailAcсount save(EmailAcсount acount) {
		return emailAccountRepository.save(acount);
	}

	@Override
	public Page<EmailAcсount> findAll(Pageable pageable) {
		return emailAccountRepository.findAll(pageable);
	}

	@Override
	public Optional<EmailAcсount> findById(Long id) {
		return emailAccountRepository.findById(id);
	}

	@Override
	public EmailAcсount update(EmailAcсount account) {
		try {
			emailAccountRepository.findById(account.getId()).orElseThrow(ObjectNotFoundException::new);
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
		}
		return emailAccountRepository.save(account);
	}

}