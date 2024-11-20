package de.lewens_markisen.repository.local;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.local_db.email.EmailAccountLss;

public interface EmailAccountRepository extends JpaRepository<EmailAccountLss, Long>{

}
