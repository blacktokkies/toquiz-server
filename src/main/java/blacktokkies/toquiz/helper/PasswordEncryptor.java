package blacktokkies.toquiz.helper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncryptor {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encryptPassword(String password){
        return passwordEncoder.encode(password);
    }

    public static boolean matchPassowrd(String password, String encryptedPassword){
        return passwordEncoder.matches(password, encryptedPassword);
    }
}
