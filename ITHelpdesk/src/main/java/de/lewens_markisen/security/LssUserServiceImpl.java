package de.lewens_markisen.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LssUserServiceImpl implements LssUserService{

	private final LewensportalRepository lewensportalRepository;
	
	public LssUserServiceImpl(LewensportalRepository lewensportalRepository) {
		super();
		this.lewensportalRepository = lewensportalRepository;
	}
	
	@Override
	public Optional<LssUser> findUserByName(String username) {
		if (username == null || username.isBlank()) {
			return Optional.empty();
		}
		return lewensportalRepository.findUserByName(username);
	}

	@Override
	public String getLssHashPassword(String password) {
//		return entcodeSha512(entcodeWirlpool(password));
		return null;
	}

	@Override
	public String entcodeSha512(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] messageDigest = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : messageDigest) {
            sb.append(String.format("%02x", b));
        }
		return sb.toString();
	}

	@Override
	public String entcodeWirlpool(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("Whirlpool");
        byte[] messageDigest = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : messageDigest) {
            sb.append(String.format("%02x", b));
        }
		return sb.toString();
	}
}
