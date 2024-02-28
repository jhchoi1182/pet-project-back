package com.springboot.petProject.util;

import com.springboot.petProject.dto.UserDto;
import com.springboot.petProject.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String requestURI = request.getRequestURI();
        List<String> allowedUris = Arrays.asList("/api/user", "/api/user/logout", "/api/user/delete");

        AntPathMatcher pathMatcher = new AntPathMatcher();

        if (!allowedUris.contains(requestURI) && pathMatcher.match("/api/user/**", requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = null;
        String authorizationToken = "Access_Token";

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (authorizationToken.equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            log.error("Token is missing from cookies");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if(JwtTokenUtils.isExpired(token, secretKey)) {
                log.error("Token is expired");
                filterChain.doFilter(request, response);
                return;
            }

            String nickname = JwtTokenUtils.getNickname(token, secretKey);
            String password = JwtTokenUtils.getPassword(token, secretKey);

            UserDto user = JwtTokenUtils.validatedUser(userRepository, nickname, password);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (RuntimeException error) {
            log.error("Error occurs while validating, {}", error.toString());
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
