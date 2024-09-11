package de.lewens_markisen.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.lewens_markisen.domain.Access;
import de.lewens_markisen.repositories.AccessRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccessServiceImpl implements AccessService {

	private final AccessRepository accessRepository;

	@Override
	@Transactional()
	public Access save(Access access) {
		return accessRepository.save(access);
	}

	@Override
	@Transactional()
	public Access updatePerson(Access access) {
		accessRepository.findById(access.getId());
		return accessRepository.save(access);
	}

	@Override
	public Optional<Access> findById(Long id) {
		return accessRepository.findById(id);
	}

	@Override
	public Page<Access> findAll(Pageable pageable) {
		return accessRepository.findAll(pageable);
	}

}
