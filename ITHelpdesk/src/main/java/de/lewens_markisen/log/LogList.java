package de.lewens_markisen.log;

import java.util.ArrayList;
import java.util.List;

import de.lewens_markisen.domain.local_db.Log;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LogList {

	private List<Log> logs;

	@XmlElement
	public List<Log> getUserSpringList() {
		if (logs == null) {
			logs = new ArrayList<>();
		}
		return logs;
	}

}
