package de.lewens_markisen.log;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.lewens_markisen.domain.localDb.Log;

public interface LogService {
	public void save(Log log);

	public void deleteAltRecords();

	public Page<Log> findAll(Pageable pageable);
}
