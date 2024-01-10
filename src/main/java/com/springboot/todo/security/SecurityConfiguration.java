package com.springboot.todo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.authorizeHttpRequests(
                    auth -> auth
                            .requestMatchers(
                                    HttpMethod.POST,
                                    "/user/signup",
                                    "/user/login"
                            ).permitAll()
                            .requestMatchers(
                                    HttpMethod.DELETE,
                                    "/user/delete/{id}"
                            ).permitAll()
                            .anyRequest()
                            .authenticated())
                .sessionManagement(
                    session ->
                            session.sessionCreationPolicy(
                                    SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                // TODO
//                .exceptionHandling()
//                .authenticationEntryPoint()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
