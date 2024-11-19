package de.lewens_markisen.repository.local;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.local_db.email.EmailAcсount;

public interface EmailAccountRepository extends JpaRepository<EmailAcсount, Long>{

}
