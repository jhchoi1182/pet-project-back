package com.springboot.petProject.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSocialLoginRequest {

    private String code;
    private String redirectUri;

}
