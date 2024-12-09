package de.lewens_markisen.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import de.lewens_markisen.utils.StringUtilsLss;

@Component
public class LssUserServiceImpl implements LssUserService {

	private final LewensportalRepository lewensportalRepository;

	public LssUserServiceImpl(LewensportalRepository lewensportalRepository) {
		super();
		this.lewensportalRepository = lewensportalRepository;
	}

	@Override
	public Optional<List<LssUser>> findUserByName(String username) {
		if (username == null || username.isBlank()) {
			return Optional.empty();
		}
		return lewensportalRepository.findUserByName(username);
	}

	@Override
	public String getLssHashPassword(String password, String salt) throws NoSuchAlgorithmException {
		return hash("SHA-512", hash("WHIRLPOOL", password + salt));
	}

	@Override
	public String hash(String codec, String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(codec);
		byte[] messageDigest = md.digest(password.getBytes());
		StringBuilder sb = new StringBuilder();
		for (byte b : messageDigest) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	@Override
	public Optional<String> getBcCodeByUsername(String username) {
		if (username == null || username.isBlank()) {
			return Optional.empty();
		}
		return lewensportalRepository.getBcCodeByUsername(username);
	}

	@Override
	public Optional<Map<String, Object>> getProfileAttributsByLssUser(String username) {
		// firstname, lastname, bcCode
		return lewensportalRepository.getProfileAttributsByUserId(StringUtilsLss.convertNameToLowCase(username));
	}
}
