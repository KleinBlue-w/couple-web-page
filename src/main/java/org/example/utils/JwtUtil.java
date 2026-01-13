package org.example.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRE_MS = 24 * 60 * 60 * 1000; // 1 天

    public String createToken(Long userId) {
        return Jwts.builder().setSubject(String.valueOf(userId)).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRE_MS)).signWith(KEY).compact();
    }

    public Long getUserId(String token) {
        return Long.valueOf(Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).getBody().getSubject());
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}