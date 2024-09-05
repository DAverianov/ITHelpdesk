package de.lewens_markisen.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Component
public class ConnectionBC implements ConnectionWeb{
	@Value("${businesscentral.url}")
	private String url;
	@Autowired
	private AuthenticatorInitializer authenticator;
	
	@Override
	public String getFilter(String attribute, String value) {
		return "?$filter="+attribute+" eq "+"'"+value+"'";
	}
	
}
