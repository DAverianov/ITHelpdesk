package de.lewens_markisen.email.model;

import java.util.ArrayList;
import java.util.List;

import de.lewens_markisen.domain.local_db.email.EmailAcсount;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EmailAccounts {

	private List<EmailAcсount> accounts;

	@XmlElement
	public List<EmailAcсount> getAccountList() {
		if (accounts == null) {
			accounts = new ArrayList<>();
		}
		return accounts;
	}

}
