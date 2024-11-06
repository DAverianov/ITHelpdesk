package de.lewens_markisen.timeRegisterEvent;

import java.util.Optional;

import de.lewens_markisen.domain.local_db.time_register_event.Pause;

public interface PauseService {
	public Optional<Pause> findByName(String name);

	public long count();

	public Pause save(Pause pause);

}
