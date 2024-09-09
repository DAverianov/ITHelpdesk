package de.lewens_markisen.services;

import de.lewens_markisen.domain.TimeRegisterEvent;

public interface TimeRegisterEventService {

    Iterable<TimeRegisterEvent> findAll(Long personId);

}
