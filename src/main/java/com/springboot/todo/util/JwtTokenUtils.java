package com.springboot.todo.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtils {

    public static String generateToken(String username, String encodedPassword, String key, long expiredTimeMs) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("password", encodedPassword);

        long nowMillis = System.currentTimeMillis();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(nowMillis))
                .expiration(new Date(nowMillis + expiredTimeMs))
                .signWith(getKey(key))
                .compact();
    }

    private static Key getKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
