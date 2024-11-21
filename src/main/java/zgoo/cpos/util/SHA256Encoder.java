package zgoo.cpos.util;

import org.springframework.security.crypto.password.PasswordEncoder;

public class SHA256Encoder implements PasswordEncoder{

    @Override
    public String encode(CharSequence raqPassword) {
        return EncryptionUtils.encryptSHA256(raqPassword.toString());
    }

    @Override
    public boolean matches(CharSequence raqPassword, String encodePassword) {
        String encryptedPassword = EncryptionUtils.encryptSHA256(raqPassword.toString());
        return encryptedPassword.equals(encodePassword);
    }
}
