package de.lewens_markisen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lewens_markisen.domain.TimeRegisterEvent;

public interface TimeRegisterEventRepository extends JpaRepository<TimeRegisterEvent, Long>{

}
