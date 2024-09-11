package de.lewens_markisen.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.lewens_markisen.domain.Access;

public interface AccessService {
	public Access save(Access access);
	public Optional<Access> findById(Long id);
	public Page<Access> findAll(Pageable pageable);
	public Access update(Access access);
}
