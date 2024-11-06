package de.lewens_markisen.repository.local;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.local_db.time_register_event.Pause;

public interface PauseRepository extends JpaRepository<Pause, Long>{

	Optional<Pause> findByName(String name);

}
