package com.springboot.todo.util;

import com.springboot.todo.dto.UserDto;
import com.springboot.todo.exception.ErrorCode;
import com.springboot.todo.exception.TodoExceptionHandler;
import com.springboot.todo.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtTokenUtils {

    public static UserDto validatedUser(UserRepository userRepository, String username, String password) {
        UserDto user = UserDto.fromEntity(userRepository.findByUsername(username).orElseThrow(() ->
                new TodoExceptionHandler(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", username))));

        if (!password.equals(user.getPassword())) {
            throw new TodoExceptionHandler(ErrorCode.INVALID_INFO, "Invalid password");
        }

        return user;
    }

    public static String getUsername(String token, String key) {
        return extractClaims(token, key).get("username", String.class);
    }

    public static String getPassword(String token, String key) {
        return extractClaims(token, key).get("password", String.class);
    }

    public static boolean isExpired(String token, String key) {
        Date expiredDate = extractClaims(token, key).getExpiration();
        return expiredDate.before(new Date());
    }

    public static Claims extractClaims(String token, String key) {
        return Jwts.parser().verifyWith((SecretKey) getKey(key))
                .build().parseSignedClaims(token).getPayload();
    }

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
