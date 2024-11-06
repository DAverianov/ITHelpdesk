package de.lewens_markisen.timeRegisterEvent;

import java.util.Optional;

import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.local_db.time_register_event.Pause;
import de.lewens_markisen.repository.local.PauseRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PauseServiceImpl implements PauseService{
	private final PauseRepository pauseRepository;

	@Override
	public Optional<Pause> findByName(String name) {
		return pauseRepository.findByName(name);
	}

	@Override
	public long count() {
		return pauseRepository.count();
	}

	@Override
	public Pause save(Pause pause) {
		return pauseRepository.save(pause);
	}

}
