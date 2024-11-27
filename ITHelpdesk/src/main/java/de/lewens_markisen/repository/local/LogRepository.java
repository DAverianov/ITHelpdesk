package de.lewens_markisen.repository.local;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.lewens_markisen.domain.local_db.Log;

public interface LogRepository extends JpaRepository<Log, Long>{

	@Query(value = "SELECT l.* FROM Log l WHERE DATE(created_date) <=:date", nativeQuery = true)
	List<Log> findAllByForCreationDate(LocalDate date);

}
