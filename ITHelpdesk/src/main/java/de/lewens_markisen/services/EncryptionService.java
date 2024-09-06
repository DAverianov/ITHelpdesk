package de.lewens_markisen.services;

public interface EncryptionService {

    String encrypt(String freeText);

    String decrypt(String encryptedText);
}
