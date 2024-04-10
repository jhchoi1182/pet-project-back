package com.springboot.petProject.util;

import com.springboot.petProject.dto.UserDto;
import com.springboot.petProject.exception.ErrorCode;
import com.springboot.petProject.exception.CustomExceptionHandler;
import com.springboot.petProject.repository.UserRepository;
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

    public static UserDto validatedUser(UserRepository userRepository, String username, String nickname) {
        return UserDto.fromEntity(userRepository.findByUsername(username).orElseThrow(() ->
                new CustomExceptionHandler(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", username))));
    }

    public static String getUsername(String token, String key) {
        return extractClaims(token, key).get("username", String.class);
    }

    public static String getNickname(String token, String key) {
        return extractClaims(token, key).get("nickname", String.class);
    }

    public static boolean isExpired(String token, String key) {
        Date expiredDate = extractClaims(token, key).getExpiration();
        return expiredDate.before(new Date());
    }

    public static Claims extractClaims(String token, String key) {
        return Jwts.parser().verifyWith((SecretKey) getKey(key))
                .build().parseSignedClaims(token).getPayload();
    }

    public static String generateToken(String username, String nickname, String key, long expiredTimeMs) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("nickname", nickname);
        claims.put("username", username);

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
