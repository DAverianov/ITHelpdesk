package de.lewens_markisen.security;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;

public interface LssUserService{
	public Optional<LssUser> findUserByName(String username);
	public String getLssHashPassword(String password, String salt) throws NoSuchAlgorithmException;
	public String hash(String codec, String password) throws NoSuchAlgorithmException;
	public Optional<String> getBcCodeByUsername(String username);
	public Optional<Map<String, Object>> getProfileAttributsByLssUser(String username);
}
