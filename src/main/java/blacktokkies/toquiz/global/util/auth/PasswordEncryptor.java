package blacktokkies.toquiz.global.util.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncryptor {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encryptPassword(String password){
        return passwordEncoder.encode(password);
    }

    public static boolean matchPassword(String password, String encryptedPassword){
        return passwordEncoder.matches(password, encryptedPassword);
    }
}
