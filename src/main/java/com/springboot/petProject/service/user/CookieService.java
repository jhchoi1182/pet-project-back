package com.springboot.petProject.service.user;

import com.springboot.petProject.dto.service.AuthDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieService {

    private final UserService userService;
    private final Environment env;

    public AuthDto extractAuthenticationAndSetHeaderCookie(Authentication authentication, HttpServletResponse response, Boolean expired) {
        return userService.extractNicknameAndTokenFromAuthentication(authentication);
    }
}
