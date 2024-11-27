package de.lewens_markisen.repository.local;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.lewens_markisen.domain.local_db.email.EmailLetter;

public interface EmailLetterRepository extends JpaRepository<EmailLetter, Long>{

	@Query("SELECT em FROM EmailLetter em WHERE (em.send is null or em.send = :send)")
	List<EmailLetter> findAllBySend(boolean send);

}
