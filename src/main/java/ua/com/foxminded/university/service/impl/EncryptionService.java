package ua.com.foxminded.university.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionService.class);
    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private EncryptionService() {
    }

    public static String encrypt(String password) {
        String encryptedPass = new String();
        try {
            encryptedPass = bCryptPasswordEncoder.encode(password);
        } catch (IllegalArgumentException illegalArgumentException) {
            LOGGER.error("Failed to encrypt", illegalArgumentException);
        }
        return encryptedPass;
    }

    public static boolean match(String raw, String encrypted) {
        return bCryptPasswordEncoder.matches(raw, encrypted);
    }
}
