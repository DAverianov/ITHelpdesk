package de.lewens_markisen.security;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface LssUserService {
	public Optional<LssUser> findUserByName(String username);
	public String getLssHashPassword(String password);
	public String entcodeSha512(String password) throws NoSuchAlgorithmException;
	public String entcodeWirlpool(String password) throws NoSuchAlgorithmException;
}
