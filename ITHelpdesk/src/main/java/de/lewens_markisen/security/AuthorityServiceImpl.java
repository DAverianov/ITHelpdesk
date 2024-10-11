package de.lewens_markisen.security;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.localDb.security.Authority;
import de.lewens_markisen.repository.local.security.AuthorityRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthorityServiceImpl implements AuthorityService{
	private final AuthorityRepository authorityRepository;

	@Override
	public Authority save(Authority auth) {
		List<Authority> authWithThatName = authorityRepository.findByPermission(auth.getPermission());
		Long qua = authWithThatName.stream().filter(u -> !u.getId().equals(auth.getId())).collect(Collectors.counting());
		if (qua==0) {
			return authorityRepository.save(auth);
		}
		else {
			throw new BadRequestException( List.of("Authority with Permission "+auth.getPermission()+" is allready exist!"));
		}
	}

	@Override
	public Optional<Authority> findByPermission(String permission) {
		return authorityRepository.findFirstByPermission(permission);
	}

	@Override
	public Authority saveIfNotExist(Authority auth) {
		Optional<Authority> authOpt = findByPermission(auth.getPermission());
		if (authOpt.isPresent()) {
			return authOpt.get();
		}
		else {
			return save(auth);
		}
	}

	@Override
	public long count() {
		return authorityRepository.count();
	}

}
