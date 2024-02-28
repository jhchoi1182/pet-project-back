package com.springboot.petProject.service.user;

import com.springboot.petProject.dto.service.AuthDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class CookieService {

    private final UserService userService;

    public AuthDto extractAuthenticationAndSetHeaderCookie(Authentication authentication, HttpServletResponse response, Boolean expired) {
        AuthDto authDto = userService.extractNicknameAndTokenFromAuthentication(authentication);
        setHeaderAuthenticationCookie(response, authDto.getToken(), expired);
        return authDto;
    }

    public void setHeaderAuthenticationCookie(HttpServletResponse response, String token, Boolean expired) {
        ResponseCookie cookie = ResponseCookie.from("Access_Token", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(expired ? 0 : 7 * 24 * 60 * 60)
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
