package org.cityu.supermarket.util;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGeneratorTest {
    
    @Test
    void generatePassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "123456";
        String encoded = encoder.encode(rawPassword);
        
        System.out.println("==========================================");
        System.out.println("raw password: " + rawPassword);
        System.out.println("encoded password: " + encoded);
        System.out.println("verification: " + encoder.matches(rawPassword, encoded));
        System.out.println("==========================================");
    }
}
