package org.example.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    private static BCryptPasswordEncoder ENCODER;

    @Autowired
    public void setEncoder(BCryptPasswordEncoder encoder) {
        ENCODER = encoder;
    }

    public static String encrypt(String raw) {
        return ENCODER.encode(raw);
    }

    public static boolean matches(String raw, String enc) {
        return ENCODER.matches(raw, enc);
    }
}
