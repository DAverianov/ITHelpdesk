package de.lewens_markisen.timeRegisterEvent;

import java.util.List;
import java.util.Optional;

import de.lewens_markisen.domain.local_db.time_register_event.DayArt;

public interface DayArtService {
	public Optional<DayArt> findByName(String name);
	public long count();
	public DayArt save(DayArt datArt);
	public List<DayArt> findAll();
	public Optional<DayArt> findById(Long id);
}
