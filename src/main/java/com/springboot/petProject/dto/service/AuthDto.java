package com.springboot.petProject.dto.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthDto {
    String token;
    String nickname;
}
