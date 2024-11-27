package de.lewens_markisen.repository.local;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.local_db.time_register_event.DayArt;

public interface DayArtRepository extends JpaRepository<DayArt, Long>{
	public Optional<DayArt> findByName(String name);
    public List<DayArt> findAllByOrderByName();
}
