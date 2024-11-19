package de.lewens_markisen.repository.local;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.local_db.email.EmailLetter;

public interface EmailLetterRepository extends JpaRepository<EmailLetter, Long>{

}
