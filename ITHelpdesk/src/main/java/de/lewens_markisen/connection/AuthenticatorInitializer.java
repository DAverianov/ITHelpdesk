package de.lewens_markisen.connection;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Component
public class AuthenticatorInitializer {
	@Value("${businesscentral.ntlm.user}")
	String username;

	@Value("${businesscentral.ntlm.password}")
	String password;

	@PostConstruct
	void initializeAuthenticator() {
		Authenticator.setDefault(new NtlmAuthenticator(username, password));
	}

	static class NtlmAuthenticator extends Authenticator {
		private PasswordAuthentication passwordAuthentication;

		NtlmAuthenticator(final String username, final String password) {
			this.passwordAuthentication = new PasswordAuthentication(username, password.toCharArray());
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return (this.getRequestingScheme().trim().equalsIgnoreCase("NTLM")) ? this.passwordAuthentication : null;
		}
	}
}
