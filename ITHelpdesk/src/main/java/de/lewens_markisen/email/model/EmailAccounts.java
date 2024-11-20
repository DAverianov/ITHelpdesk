package de.lewens_markisen.email.model;

import java.util.ArrayList;
import java.util.List;

import de.lewens_markisen.domain.local_db.email.EmailAccountLss;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EmailAccounts {

	private List<EmailAccountLss> accounts;

	@XmlElement
	public List<EmailAccountLss> getAccountList() {
		if (accounts == null) {
			accounts = new ArrayList<>();
		}
		return accounts;
	}

}
