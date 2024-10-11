package de.lewens_markisen.access;

import java.util.ArrayList;
import java.util.List;

import de.lewens_markisen.domain.localDb.Access;
import de.lewens_markisen.domain.localDb.Person;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Accesses {

	private List<Access> accesses;

	@XmlElement
	public List<Access> getAccessList() {
		if (accesses == null) {
			accesses = new ArrayList<>();
		}
		return accesses;
	}

}
