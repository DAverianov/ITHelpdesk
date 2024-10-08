package de.lewens_markisen.log;

import de.lewens_markisen.domain.localDb.Log;

public interface LogService {
	public void save(Log log);

	public void deleteAltRecords();
}
