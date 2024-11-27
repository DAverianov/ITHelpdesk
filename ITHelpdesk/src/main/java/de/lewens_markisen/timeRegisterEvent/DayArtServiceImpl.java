package de.lewens_markisen.timeRegisterEvent;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.local_db.time_register_event.DayArt;
import de.lewens_markisen.repository.local.DayArtRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DayArtServiceImpl implements DayArtService{
	private final DayArtRepository dayArtRepository;

	@Override
	public Optional<DayArt> findByName(String name) {
		return dayArtRepository.findByName(name);
	}

	@Override
	public long count() {
		return dayArtRepository.count();
	}

	@Override
	public DayArt save(DayArt dayArt) {
		return dayArtRepository.save(dayArt);
	}

	@Override
	public List<DayArt> findAll() {
		return dayArtRepository.findAllByOrderByName();
	}

	@Override
	public Optional<DayArt> findById(Long id) {
		return dayArtRepository.findById(id);
	}

}
