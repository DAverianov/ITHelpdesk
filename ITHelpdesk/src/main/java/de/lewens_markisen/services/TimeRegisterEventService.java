package de.lewens_markisen.services;

import java.util.List;

import de.lewens_markisen.domain.TimeRegisterEvent;

public interface TimeRegisterEventService {

    List<TimeRegisterEvent> findAll(Long personId);

}
